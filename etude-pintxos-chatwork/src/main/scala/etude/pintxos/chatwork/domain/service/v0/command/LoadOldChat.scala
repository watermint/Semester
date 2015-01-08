package etude.pintxos.chatwork.domain.service.v0.command

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.service.v0.Api
import etude.pintxos.chatwork.domain.service.v0.parser.MessageParser
import etude.pintxos.chatwork.domain.service.v0.request.LoadOldChatRequest
import etude.pintxos.chatwork.domain.service.v0.response.LoadOldChatResponse

import scala.concurrent.Future

object LoadOldChat
  extends ChatWorkCommand[LoadOldChatRequest, LoadOldChatResponse] {

  def execute(request: LoadOldChatRequest)(implicit context: EntityIOContext[Future]): LoadOldChatResponse = {
    implicit val executor = getExecutionContext(context)

    val json = Api.api(
      "load_old_chat",
      Map(
        "room_id" -> request.lastMessage.roomId.value.toString(),
        "first_chat_id" -> request.lastMessage.messageId.toString()
      )
    )

    LoadOldChatResponse(
      json,
      request.lastMessage,
      MessageParser.parseMessage(request.lastMessage.roomId, json)
    )
  }
}
