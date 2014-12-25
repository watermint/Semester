package etude.pintxos.chatwork.domain.infrastructure.api.v0.command

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.infrastructure.api.v0.V0AsyncApi
import etude.pintxos.chatwork.domain.infrastructure.api.v0.parser.MessageParser
import etude.pintxos.chatwork.domain.infrastructure.api.v0.request.LoadOldChatRequest
import etude.pintxos.chatwork.domain.infrastructure.api.v0.response.LoadOldChatResponse
import etude.pintxos.chatwork.domain.model.message.{Message, MessageId}

import scala.concurrent.Future

object LoadOldChat
  extends ChatWorkCommand[LoadOldChatRequest, LoadOldChatResponse] {

  def execute(request: LoadOldChatRequest)(implicit context: EntityIOContext[Future]): Future[LoadOldChatResponse] = {
    implicit val executor = getExecutionContext(context)

    V0AsyncApi.api(
      "load_old_chat",
      Map(
        "room_id" -> request.lastMessage.roomId.value.toString(),
        "first_chat_id" -> request.lastMessage.messageId.toString()
      )
    ) map {
      json =>
        LoadOldChatResponse(
          json,
          MessageParser.parseMessage(request.lastMessage.roomId, json)
        )
    }
  }
}
