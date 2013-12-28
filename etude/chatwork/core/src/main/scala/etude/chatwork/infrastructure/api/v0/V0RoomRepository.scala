package etude.chatwork.infrastructure.api.v0

import etude.chatwork.domain.room._
import etude.chatwork.domain.message.MessageId
import org.json4s._
import scala.concurrent._
import etude.foundation.domain.lifecycle.EntityIOContext
import etude.chatwork.infrastructure.api.NotImplementedException

class V0RoomRepository
  extends AsyncRoomRepository {

  type This <: V0RoomRepository

  def resolve(identity: RoomId)(implicit context: EntityIOContext[Future]): Future[Room] =
    Future.failed(NotImplementedException("Migrated to v1 API"))

  def containsByIdentity(identity: RoomId)(implicit context: EntityIOContext[Future]): Future[Boolean] =
    Future.failed(NotImplementedException("Migrated to v1 API"))

  def latestMessage(roomId: RoomId)(implicit context: EntityIOContext[Future]): Future[MessageId] = {
    implicit val executor = getExecutionContext(context)
    V0AsyncApi.api(
      "load_chat",
      Map(
        "room_id" -> roomId.value.toString(),
        "first_chat_id" -> "0",
        "last_chat_id" -> "0",
        "jump_to_chat_id" -> "0",
        "unread_num" -> "0",
        "desc" -> "0",
        "task" -> "0"
      )
    ) map {
      json =>
        val messageIds: List[BigInt] = for {
          JObject(data) <- json
          JField("chat_list", JArray(messages)) <- data
          JObject(message) <- messages
          JField("id", JInt(messageId)) <- message
        } yield {
          messageId
        }
        new MessageId(roomId, messageIds.maxBy(identity))
    }
  }

  def markAsRead(message: MessageId)(implicit context: EntityIOContext[Future]): Future[MessageId] = {
    implicit val executor = getExecutionContext(context)
    V0AsyncApi.api(
      "read",
      Map(
        "room_id" -> message.roomId.value.toString(),
        "last_chat_id" -> message.messageId.toString()
      )
    ) map {
      json =>
        message
    }
  }
}
