package etude.pintxos.chatwork.domain.service.v0.command

import etude.pintxos.chatwork.domain.model.message.MessageId
import etude.pintxos.chatwork.domain.service.v0.parser.MessageParser
import etude.pintxos.chatwork.domain.service.v0.request.LoadChatRequest
import etude.pintxos.chatwork.domain.service.v0.response.LoadChatResponse
import etude.pintxos.chatwork.domain.service.v0.{ChatWorkApi, ChatWorkIOContext}
import org.json4s.JsonAST.{JField, JObject, JString}

object LoadChat
  extends ChatWorkCommand[LoadChatRequest, LoadChatResponse] {

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

  def execute(request: LoadChatRequest)(implicit context: ChatWorkIOContext): LoadChatResponse = {

    val json = ChatWorkApi.api(
      "load_chat",
      Map(
        "room_id" -> request.room.value.toString(),
        "first_chat_id" -> toChatId(request.firstChatId),
        "last_chat_id" -> toChatId(request.lastChatId),
        "jump_to_chat_id" -> toChatId(request.jumpToChatId),
        "unread_num" -> fromBoolean(request.unreadNum),
        "desc" -> fromBoolean(request.description),
        "task" -> fromBoolean(request.task)
      )
    )

    val results: Seq[LoadChatResponse] = for {
      JObject(j) <- json
      JField("chat_list", chatList) <- j
    } yield {
      val m = j.toMap

      LoadChatResponse(
        json,
        MessageParser.parseMessage(request.room, chatList),
        m.get("description") collect { case JString(d) => d},
        m.get("public_description") collect { case JString(d) => d}
      )
    }
    results.last
  }
}
