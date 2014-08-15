package etude.pintxos.chatwork.domain.infrastructure.api.v0.command

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.infrastructure.api.v0.{V0AsyncApi, V0AsyncEntityIO}
import etude.pintxos.chatwork.domain.model.message.MessageId
import etude.pintxos.chatwork.domain.model.room.RoomId

import scala.concurrent.Future

object LoadChat extends V0AsyncEntityIO {

  case class LoadChatResult()

  private def toChatId(message: Option[MessageId]): String = {
    message match {
      case Some(m) => m.messageId.toString()
      case _ => "0"
    }
  }

  private def fromBoolean(value: Boolean): String = {
    if (value) {
      "1"
    } else {
      "0"
    }
  }

  def loadChat(room: RoomId,
               firstChatId: Option[MessageId] = None,
               lastChatId: Option[MessageId] = None,
               jumpToChatId: Option[MessageId] = None,
               unreadNum: Boolean = false,
               description: Boolean = false,
               task: Boolean = false)
              (implicit context: EntityIOContext[Future]): Future[LoadChatResult] = {
    implicit val executor = getExecutionContext(context)

    V0AsyncApi.api(
      "load_chat",
      Map(
        "room_id" -> room.value.toString(),
        "first_chat_id" -> toChatId(firstChatId),
        "last_chat_id" -> toChatId(lastChatId),
        "jump_to_chat_id" -> toChatId(jumpToChatId),
        "unread_num" -> fromBoolean(unreadNum),
        "desc" -> fromBoolean(description),
        "task" -> fromBoolean(task)
      )
    ) map {
      json =>
        LoadChatResult()
    }
  }
}
