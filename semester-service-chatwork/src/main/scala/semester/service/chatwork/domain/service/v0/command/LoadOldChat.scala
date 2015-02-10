package semester.service.chatwork.domain.service.v0.command

import semester.service.chatwork.domain.service.v0.parser.MessageParser
import semester.service.chatwork.domain.service.v0.request.LoadOldChatRequest
import semester.service.chatwork.domain.service.v0.response.LoadOldChatResponse
import semester.service.chatwork.domain.service.v0.{ChatWorkApi, ChatWorkIOContext}

object LoadOldChat
  extends ChatWorkCommand[LoadOldChatRequest, LoadOldChatResponse] {

  def execute(request: LoadOldChatRequest)(implicit context: ChatWorkIOContext): LoadOldChatResponse = {
    val json = ChatWorkApi.api(
      "load_old_chat",
      Map(
        "room_id" -> request.lastMessage.roomId.value.toString(),
        "first_chat_id" -> request.lastMessage.messageId.toString()
      )
    )

    LoadOldChatResponse(
      json,
      request,
      request.lastMessage,
      MessageParser.parseMessage(request.lastMessage.roomId, json)
    )
  }
}
