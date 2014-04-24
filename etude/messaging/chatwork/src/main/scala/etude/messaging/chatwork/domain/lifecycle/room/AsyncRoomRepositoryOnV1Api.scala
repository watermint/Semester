package etude.messaging.chatwork.domain.lifecycle.room

import java.time.Instant
import java.net.URI
import org.json4s._
import etude.messaging.chatwork.domain.model.room._
import scala.Some
import etude.messaging.chatwork.domain.model.room.RoomAttributes
import etude.messaging.chatwork.domain.model.message.MessageId
import etude.foundation.domain.lifecycle.{EntityIOContext, EntityNotFoundException}
import scala.concurrent.Future
import etude.messaging.chatwork.domain.infrastructure.api.v1.{V1ApiException, V1AsyncApi}
import etude.messaging.chatwork.domain.infrastructure.api.v0.V0ApiQoS

private[room]
class AsyncRoomRepositoryOnV1Api
  extends AsyncRoomRepository {

  def latestMessage(roomId: RoomId)(implicit context: EntityIOContext[Future]): Future[MessageId] = ???

  private val ENDPOINT_ROOMS = "/v1/rooms"

  protected def parseRoom(result: JValue): List[Room] = {
    for {
      JObject(data) <- result
      JField("room_id", JInt(roomId)) <- data
      JField("name", JString(name)) <- data
      JField("type", JString(roomType)) <- data
      JField("role", JString(roomRole)) <- data
      JField("sticky", JBool(sticky)) <- data
      JField("unread_num", JInt(unreadNum)) <- data
      JField("mention_num", JInt(mentionNum)) <- data
      JField("mytask_num", JInt(myTaskNum)) <- data
      JField("message_num", JInt(messageNum)) <- data
      JField("file_num", JInt(fileNum)) <- data
      JField("task_num", JInt(taskNum)) <- data
      JField("icon_path", JString(iconPath)) <- data
      JField("last_update_time", JInt(lastUpdateTime)) <- data
    } yield {
      new Room(
        roomId = RoomId(roomId),
        name = name,
        description = None,
        roomType = RoomType(roomType),
        attributes = Some(RoomAttributes(
          sticky = sticky,
          unreadCount = unreadNum,
          mentionCount = mentionNum,
          myTaskCount = myTaskNum,
          totalTaskCount = taskNum,
//          totalMessageCount = messageNum,
          fileCount = fileNum
        )),
        avatar = Some(new URI(iconPath)),
        lastUpdateTime = Some(Instant.ofEpochSecond(lastUpdateTime.toLong))
      )
    }
  }

  //  def create(name: String,
  //             roles: List[RoomRole],
  //             description: String,
  //             icon: RoomIcon): Try[RoomId] = {
  //
  //    if (!roles.exists(_.roleType.isInstanceOf[RoomRoleAdmin])) {
  //      return Failure(AdminAccountRequiredException(s"No admin user found"))
  //    }
  //
  //    val operation = s"PUT $ENDPOINT_ROOMS"
  //    if (shouldFail(operation)) {
  //      return Failure(QoSException(operation))
  //    }
  //
  //    try {
  //      V1AsyncApi.put(
  //        path = ENDPOINT_ROOMS,
  //        data = List(
  //          "description" -> description,
  //          "icon_preset" -> icon.name,
  //          "member_admin_ids" -> roles.filter(_.roleType.isInstanceOf[RoomRoleAdmin]).map(_.roomRoleId.accountId.value).mkString(","),
  //          "member_member_ids" -> roles.filter(_.roleType.isInstanceOf[RoomRoleMember]).map(_.roomRoleId.accountId.value).mkString(","),
  //          "member_readonly_ids" -> roles.filter(_.roleType.isInstanceOf[RoomRoleReadonly]).map(_.roomRoleId.accountId.value).mkString(",")
  //        )
  //      ) match {
  //        case Failure(f) => Failure(f)
  //        case Success(json) =>
  //          val result: List[BigInt] = for {
  //            JObject(data) <- json
  //            JField("room_id", JInt(roomId)) <- data
  //          } yield {
  //            roomId
  //          }
  //
  //          result.lastOption match {
  //            case Some(r) => Success(new RoomId(r))
  //            case _ => Failure(V1ApiException("Failed to create room"))
  //          }
  //      }
  //    } finally {
  //      lastLoad.put(operation, Instant.now)
  //    }
  //  }
  //
  //  def rooms(): Try[List[Room]] = {
  //    val operation = s"GET $ENDPOINT_ROOMS"
  //    if (shouldFail(operation)) {
  //      return Failure(QoSException(operation))
  //    }
  //
  //    try {
  //      V1AsyncApi.get(ENDPOINT_ROOMS) match {
  //        case Success(r) => Success(parseRoom(r))
  //        case Failure(f) => Failure(f)
  //      }
  //    } finally {
  //      lastLoad.put(operation, Instant.now)
  //    }
  //  }


  def create(name: String, description: String, icon: RoomIcon)(implicit context: EntityIOContext[Future]): Future[RoomId] = ???

  def myRoom()(implicit context: EntityIOContext[Future]): Future[Room] = {
    implicit val executor = getExecutionContext(context)
    rooms() map {
      rooms =>
        rooms.filter(r => RoomType.isMyRoom(r.roomType)).last
    }
  }

  def rooms()(implicit context: EntityIOContext[Future]): Future[List[Room]] = {
    implicit val executor = getExecutionContext(context)
    V1AsyncApi.get(ENDPOINT_ROOMS) map {
      json =>
        parseRoom(json)
    }
  }

  def resolve(identity: RoomId)(implicit context: EntityIOContext[Future]): Future[Room] = {
    implicit val executor = getExecutionContext(context)
    val endPoint = ENDPOINT_ROOMS + "/" + identity.value

    V1AsyncApi.get(endPoint) map {
      json =>
        parseRoom(json).lastOption match {
          case Some(room) =>
            val roomWithDescription: List[Room] = for {
              JObject(data) <- json
              JField("room_id", JInt(roomId)) <- data
              JField("description", JString(description)) <- data
            } yield {
              room.copy(description = Some(description))
            }
            roomWithDescription.lastOption match {
              case Some(rwd) => rwd
              case _ => room
            }
          case _ => throw EntityNotFoundException(s"Room not found for identifier: $identity")
        }
    }
  }

  def containsByIdentity(identity: RoomId)(implicit context: EntityIOContext[Future]): Future[Boolean] = {
    implicit val executor = getExecutionContext(context)
    val endPoint = ENDPOINT_ROOMS + "/" + identity.value

    V1AsyncApi.get(endPoint) map {
      json =>
        true
    } recover {
      case e: V1ApiException => false
    }
  }
}
