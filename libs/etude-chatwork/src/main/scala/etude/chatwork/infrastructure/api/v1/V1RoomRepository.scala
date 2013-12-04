package etude.chatwork.infrastructure.api.v1

import java.time.Instant
import java.net.URI
import org.json4s._
import scala.util.Try
import etude.chatwork.domain._
import etude.chatwork.infrastructure.api.{QoSException, NotImplementedException, ApiQoS}
import etude.chatwork.domain.room._
import scala.util.Failure
import scala.Some
import etude.chatwork.domain.room.RoomAttributes
import scala.util.Success
import etude.chatwork.domain.message.MessageId

case class V1RoomRepository(implicit authToken: V1AuthToken) extends RoomRepository with ApiQoS {
  private val ENDPOINT_ROOMS = "/v1/rooms"

  def markAsRead(message: MessageId): Try[MessageId] = Failure(NotImplementedException("Not implemented by ChatWork"))

  def latestMessage(roomId: RoomId): Try[MessageId] = Failure(NotImplementedException("Not implemented by ChatWork"))

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
        roomId = new RoomId(roomId),
        name = name,
        description = None,
        roomType = RoomType(roomType),
        roomRole = RoomRoleType(roomRole),
        attributes = Some(RoomAttributes(
          sticky = sticky,
          unreadCount = unreadNum,
          mentionCount = mentionNum,
          myTaskCount = myTaskNum,
          totalTaskCount = taskNum,
          totalMessageCount = messageNum,
          fileCount = fileNum
        )),
        avatar = new URI(iconPath),
        lastUpdateTime = Instant.ofEpochSecond(lastUpdateTime.toLong)
      )
    }
  }

  def create(name: String,
             roles: List[RoomRole],
             description: String,
             icon: RoomIcon): Try[RoomId] = {

    if (!roles.exists(_.roleType.isInstanceOf[RoomRoleAdmin])) {
      return Failure(AdminAccountRequiredException(s"No admin user found"))
    }

    val operation = s"PUT $ENDPOINT_ROOMS"
    if (shouldFail(operation)) {
      return Failure(QoSException(operation))
    }

    try {
      V1Api.put(
        path = ENDPOINT_ROOMS,
        data = List(
          "description" -> description,
          "icon_preset" -> icon.name,
          "member_admin_ids" -> roles.filter(_.roleType.isInstanceOf[RoomRoleAdmin]).map(_.roomRoleId.accountId.value).mkString(","),
          "member_member_ids" -> roles.filter(_.roleType.isInstanceOf[RoomRoleMember]).map(_.roomRoleId.accountId.value).mkString(","),
          "member_readonly_ids" -> roles.filter(_.roleType.isInstanceOf[RoomRoleReadonly]).map(_.roomRoleId.accountId.value).mkString(",")
        )
      ) match {
        case Failure(f) => Failure(f)
        case Success(json) =>
          val result: List[BigInt] = for {
            JObject(data) <- json
            JField("room_id", JInt(roomId)) <- data
          } yield {
            roomId
          }

          result.lastOption match {
            case Some(r) => Success(new RoomId(r))
            case _ => Failure(V1ApiException("Failed to create room"))
          }
      }
    } finally {
      lastLoad.put(operation, Instant.now)
    }
  }

  def rooms(): Try[List[Room]] = {
    val operation = s"GET $ENDPOINT_ROOMS"
    if (shouldFail(operation)) {
      return Failure(QoSException(operation))
    }

    try {
      V1Api.get(ENDPOINT_ROOMS) match {
        case Success(r) => Success(parseRoom(r))
        case Failure(f) => Failure(f)
      }
    } finally {
      lastLoad.put(operation, Instant.now)
    }
  }

  def resolve(identifier: RoomId): Try[Room] = {
    val endPoint = ENDPOINT_ROOMS + "/" + identifier.value
    val operation = s"GET $endPoint"
    if (shouldFail(operation)) {
      return Failure(QoSException(operation))
    }

    try {
      V1Api.get(endPoint) match {
        case Failure(f) => Failure(f)
        case Success(json) =>
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
                case Some(rwd) => Success(rwd)
                case _ => Success(room)
              }
            case _ => Failure(EntityNotFoundException(s"Room not found for identifier: $identifier"))
          }
      }
    } finally {
      lastLoad.put(operation, Instant.now)
    }
  }
}
