package etude.chatwork.v1.api

import etude.chatwork.v1._
import scala.util.Try
import java.time.Instant
import org.json4s._
import java.net.URI
import etude.chatwork.v1.RoomId
import scala.util.Failure
import scala.Some
import scala.util.Success
import etude.chatwork.v1.Room

case class ApiRoomRepository(implicit authToken: AuthToken) extends RoomRepository with ApiQoS {
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
      Room(
        roomId = RoomId(roomId),
        name = name,
        description = None,
        roomType = RoomType(roomType),
        roomRole = RoomRoleType(roomRole),
        sticky = sticky,
        unreadCount = unreadNum,
        mentionCount = mentionNum,
        myTaskCount = myTaskNum,
        totalTaskCount = taskNum,
        totalMessageCount = messageNum,
        fileCount = fileNum,
        avatar = new URI(iconPath),
        lastUpdateTime = Instant.ofEpochMilli(lastUpdateTime.toLong)
      )
    }
  }

  def rooms(): Try[List[Room]] = {
    if (shouldFail(ENDPOINT_ROOMS)) {
      return Failure(QoSException(ENDPOINT_ROOMS))
    }

    try {
      Api.get(ENDPOINT_ROOMS) match {
        case Success(r) => Success(parseRoom(r))
        case Failure(f) => Failure(f)
      }
    } finally {
      lastLoad.put(ENDPOINT_ROOMS, Instant.now)
    }
  }

  def resolve(identifier: RoomId): Try[Room] = {
    val endPoint = ENDPOINT_ROOMS + "/" + identifier.id
    if (shouldFail(endPoint)) {
      return Failure(QoSException(endPoint))
    }

    try {
      Api.get(endPoint) match {
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
            case _ => Failure(EntityNotFoundException("Room not found for identifier: " + identifier))
          }
      }
    } finally {
      lastLoad.put(endPoint, Instant.now)
    }
  }
}
