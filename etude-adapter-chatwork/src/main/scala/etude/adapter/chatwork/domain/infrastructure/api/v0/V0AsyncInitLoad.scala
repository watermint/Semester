package etude.adapter.chatwork.domain.infrastructure.api.v0

import java.net.URI
import java.time.Instant

import etude.domain.core.lifecycle.EntityIOContext
import etude.foundation.logging.LoggerFactory
import etude.adapter.chatwork.domain.model.account._
import etude.adapter.chatwork.domain.model.room._
import org.json4s._

import scala.collection.mutable
import scala.concurrent.Future

/**
 * facade for jumbo api 'init_load'.
 */
object V0AsyncInitLoad
  extends V0AsyncEntityIO {

  val logger = LoggerFactory.getLogger(getClass)

  private def asOptionString(value: String): Option[String] = {
    if (value == null || "".equals(value)) {
      None
    } else {
      Some(value)
    }
  }

  private def asOptionURI(base: String, part: String): Option[URI] = {
    asOptionString(part) map {
      v =>
        new URI(base + v)
    }
  }

  private def asOptionOrganization(orgId: BigInt, orgName: String): Option[Organization] = {
    Some(
      new Organization(
        OrganizationId(orgId),
        orgName
      )
    )
  }

  case class InitLoadContainer(content: V0AsyncInitLoadContents,
                               loadTime: Instant)

  private val cache = new mutable.HashMap[String, InitLoadContainer]()

  private val cacheSeconds = 300

  val roomIconUrlBase = "https://tky-chat-work-appdata.s3.amazonaws.com/icon/"
  val accountIconUrlBase = "https://tky-chat-work-appdata.s3.amazonaws.com/avatar/"

  def parseLastId(json: JValue): Option[String] = {
    val lastId: List[String] = for {
      JObject(doc) <- json
      JField("last_id", JString(lastId)) <- doc
    } yield {
      lastId
    }
    lastId.lastOption
  }

  def parseContact(contactDat: List[(String, JValue)]): List[Account] = {
    for {
      JField(accountId, JObject(contact)) <- contactDat
      JField("av", JString(avatarImage)) <- contact
      JField("cwid", JString(chatWorkId)) <- contact
      JField("gid", JInt(organizationId)) <- contact
      JField("onm", JString(organization)) <- contact
      JField("dp", JString(department)) <- contact
      JField("name", JString(name)) <- contact
    } yield {
      new Account(
        accountId = AccountId(BigInt(accountId)),
        name = asOptionString(name),
        chatWorkId = asOptionString(chatWorkId).map(ChatWorkId),
        organization = asOptionOrganization(organizationId, organization),
        department = asOptionString(department),
        avatarImage = asOptionURI(accountIconUrlBase, avatarImage)
      )
    }
  }

  def parseContacts(json: JValue): List[Account] = {
    for {
      JObject(doc) <- json
      JField("contact_dat", JObject(contactDat)) <- doc
      account <- parseContact(contactDat)
    } yield {
      account
    }
  }

  def parseRooms(json: JValue): List[Room] = {
    for {
      JObject(doc) <- json
      JField("room_dat", JObject(roomDat)) <- doc
      JField(roomId, JObject(room)) <- roomDat
      JField("tp", JInt(roomType)) <- room
      JField("lt", JInt(lastUpdateTime)) <- room
    } yield {
      val r = room.toMap
      val name = r.getOrElse("n", None) match {
        case JString(n) => n
        case _ => ""
      }
      val avatar = asOptionURI(
        roomIconUrlBase,
        r.getOrElse("ic", None) match {
          case JString(a) => a
          case _ => null
        }
      )

      /* Room Attributes
       *
       * r ... read (internal sequence number)
       * c ... current chat id (internal sequence number)
       * f ... files
       * t ... tasks
       * mt ... my tasks
       * mn ... mention count
       * s ... sticky (only appears on the room is sticky. value is 1)
       */
      val unreadCount: Int = (r.get("r"), r.get("c")) match {
        case (Some(JInt(read)), Some(JInt(current))) =>
          (current - read).toInt
        case _ =>
          0
      }

      val fileCount: Int = r.get("f") match {
        case Some(JInt(f)) => f.toInt
        case _ => 0
      }

      val taskCount: Int = r.get("t") match {
        case Some(JInt(t)) => t.toInt
        case _ => 0
      }

      val myTaskCount: Int = r.get("mt") match {
        case Some(JInt(t)) => t.toInt
        case _ => 0
      }

      val mentionCount: Int = r.get("mn") match {
        case Some(JInt(m)) => m.toInt
        case _ => 0
      }

      val sticky: Boolean = r.get("s") match {
        case Some(JInt(s)) => true
        case _ => false
      }

      new Room(
        roomId = RoomId(BigInt(roomId)),
        name = name,
        description = None,
        attributes = Some(RoomAttributes(
          sticky = sticky,
          unreadCount = unreadCount,
          mentionCount = mentionCount,
          myTaskCount = myTaskCount,
          totalTaskCount = taskCount,
          fileCount = fileCount
        )),
        roomType = roomType.toInt match {
          case 1 => RoomTypeGroup()
          case 2 => RoomTypeDirect()
          case 3 => RoomTypeMy()
          case _ => RoomTypeUnknown()
        },
        avatar = avatar,
        lastUpdateTime = lastUpdateTime.toLong match {
          case 0 => None
          case t => Some(Instant.ofEpochSecond(t))
        }
      )
    }
  }

  def parseParticipants(json: JValue): List[Participant] = {
    for {
      JObject(doc) <- json
      JField("room_dat", JObject(roomDat)) <- doc
      JField(roomId, JObject(room)) <- roomDat
      JField("m", JObject(member)) <- room
    } yield {
      val accounts: Seq[(AccountId, Int)] = member.map {
        p =>
          AccountId(BigInt(p._1)) -> (p._2 match {
            case JInt(role) => role.toInt
            case _ => 0
          })
      }

      new Participant(
        roomId = RoomId(BigInt(roomId)),
        admin = accounts.filter(_._2 == 1).map(_._1),
        member = accounts.filter(_._2 == 2).map(_._1),
        readonly = accounts.filter(_._2 == 3).map(_._1)
      )
    }
  }

  def initLoad()(implicit context: EntityIOContext[Future]): Future[V0AsyncInitLoadContents] = {
    implicit val executor = getExecutionContext(context)

    getMyId(context) match {
      case None => // nop
      case Some(myId) =>
        cache.get(myId) match {
          case None => // nop
          case Some(cached) =>
            if (Instant.now.minusSeconds(cacheSeconds).isBefore(cached.loadTime)) {
              logger.debug("init load from cache")
              return Future.successful(cached.content)
            }
        }
    }

    V0AsyncApi.api("init_load", Map()) map {
      json =>
        parseLastId(json) match {
          case Some(lastId) => setLastId(lastId, context)
          case _ =>
        }
        val content = V0AsyncInitLoadContents(
          contacts = parseContacts(json),
          rooms = parseRooms(json),
          participants = parseParticipants(json)
        )

        cache.put(
          getMyId(context).get,
          InitLoadContainer(
            content,
            Instant.now()
          )
        )
        logger.debug("init load finished")

        content
    }
  }
}
