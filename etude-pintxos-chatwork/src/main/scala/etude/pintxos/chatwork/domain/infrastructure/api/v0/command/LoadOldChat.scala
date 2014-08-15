package etude.pintxos.chatwork.domain.infrastructure.api.v0.command

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.infrastructure.api.v0.parser.MessageParser
import etude.pintxos.chatwork.domain.infrastructure.api.v0.{V0AsyncApi, V0AsyncEntityIO}
import etude.pintxos.chatwork.domain.model.message.{Message, MessageId}

import scala.concurrent.Future

object LoadOldChat extends V0AsyncEntityIO {
  def load(lastMessage: MessageId)
          (implicit context: EntityIOContext[Future]): Future[List[Message]] = {
    implicit val executor = getExecutionContext(context)

    V0AsyncApi.api(
      "load_old_chat",
      Map(
        "room_id" -> lastMessage.roomId.value.toString(),
        "first_chat_id" -> lastMessage.messageId.toString()
      )
    ) map {
      json =>
        MessageParser.parseMessage(lastMessage.roomId, json)
    }
  }
}
