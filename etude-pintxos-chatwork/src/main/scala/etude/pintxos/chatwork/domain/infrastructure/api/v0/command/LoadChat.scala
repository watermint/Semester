package etude.pintxos.chatwork.domain.infrastructure.api.v0.command

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.infrastructure.api.v0.model.LoadChatResponse
import etude.pintxos.chatwork.domain.infrastructure.api.v0.parser.MessageParser
import etude.pintxos.chatwork.domain.infrastructure.api.v0.{V0AsyncApi, V0AsyncEntityIO}
import etude.pintxos.chatwork.domain.model.message.{Message, MessageId}
import etude.pintxos.chatwork.domain.model.room.RoomId
import org.json4s.JsonAST.{JField, JObject, JString}

import scala.concurrent.Future

object LoadChat extends V0AsyncEntityIO {

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
              (implicit context: EntityIOContext[Future]): Future[LoadChatResponse] = {
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
        val results: Seq[LoadChatResponse] = for {
          JObject(j) <- json
          JField("chat_list", chatList) <- j
        } yield {
          val m = j.toMap

          LoadChatResponse(
            MessageParser.parseMessage(room, chatList),
            m.get("description") collect { case JString(d) => d },
            m.get("public_description") collect { case JString(d) => d}
          )
        }
        results.last
    }
  }
}
