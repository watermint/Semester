package etude.messaging.chatwork.domain.infrastructure.api.v0

import java.net.URI
import etude.foundation.domain.lifecycle.EntityIOContext
import etude.foundation.domain.lifecycle.async.AsyncEntityIO
import etude.messaging.chatwork.domain.model.account._
import etude.messaging.chatwork.domain.model.room._
import scala.concurrent.Future
import org.json4s._
import etude.messaging.chatwork.domain.model.account.OrganizationId
import etude.messaging.chatwork.domain.model.room.RoomId
import etude.messaging.chatwork.domain.model.account.ChatWorkId
import scala.Some
import etude.messaging.chatwork.domain.model.account.AccountId
import java.time.Instant


/**
 * facade for jumbo api 'init_load'.
 */
object V0AsyncInitLoad
  extends V0EntityIO[Future]
  with AsyncEntityIO {

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
        new URI(v)
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

  def parseLastId(json: JValue): Option[String] = {
    val lastId: List[String] = for {
      JObject(doc) <- json
      JField("last_id", JString(lastId)) <- doc
    } yield {
      lastId
    }
    lastId.lastOption
  }

  def parseContacts(json: JValue): List[Account] = {
    for {
      JObject(doc) <- json
      JField("contact_dat", JObject(contactDat)) <- doc
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
        avatarImage = asOptionURI("", avatarImage)
      )
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
        "",
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
       * s ... sticky (only appears on the room is sticky. value is 1)
       */
      val unreadCount = (r.get("r"), r.get("c")) match {
        case (Some(JInt(read)), Some(JInt(current))) =>
          current - read
        case _ =>
          0
      }

      new Room(
        roomId = RoomId(BigInt(roomId)),
        name = name,
        description = None,
        attributes = None,
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

    V0AsyncApi.api("init_load", Map()) map {
      json =>
        parseLastId(json) match {
          case Some(lastId) => setLastId(lastId, context)
          case _ =>
        }
        V0AsyncInitLoadContents(
          contacts = parseContacts(json),
          rooms = parseRooms(json),
          participants = parseParticipants(json)
        )
    }
  }
}
