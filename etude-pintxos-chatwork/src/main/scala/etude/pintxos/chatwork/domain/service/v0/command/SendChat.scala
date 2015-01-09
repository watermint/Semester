package etude.pintxos.chatwork.domain.service.v0.command

import etude.pintxos.chatwork.domain.model.room.RoomId
import etude.pintxos.chatwork.domain.service.v0.parser.{MessageParser, StorageParser}
import etude.pintxos.chatwork.domain.service.v0.request.SendChatRequest
import etude.pintxos.chatwork.domain.service.v0.response.SendChatResponse
import etude.pintxos.chatwork.domain.service.v0.{ChatWorkApi, ChatWorkIOContext}
import org.json4s._

object SendChat
  extends ChatWorkCommand[SendChatRequest, SendChatResponse] {

  def parseSendChatResult(json: JValue, room: RoomId): SendChatResponse = {
    val results: List[SendChatResponse] = for {
      JObject(j) <- json
      JField("storage", storage) <- j
      JField("storage_limit", JString(storageLimit)) <- j
    } yield {
      j.toMap.get("chat_list") match {
        case Some(chatList) =>
          SendChatResponse(
            json,
            StorageParser.parseStorage(storage),
            BigInt(storageLimit),
            MessageParser.parseMessage(room, chatList)
          )
        case _ =>
          SendChatResponse(
            json,
            StorageParser.parseStorage(storage),
            BigInt(storageLimit),
            Seq()
          )
      }
    }
    results.last
  }

  def execute(request: SendChatRequest)(implicit context: ChatWorkIOContext): SendChatResponse = {
    import org.json4s.JsonDSL._
    import org.json4s.native.JsonMethods._

    val pdata = ("text" -> request.text) ~
      ("room_id" -> request.room.value.toString()) ~
      ("edit_id" -> "0")

    val json = ChatWorkApi.api(
      "send_chat",
      Map.empty,
      Map(
        "pdata" -> compact(render(pdata))
      )
    )
    parseSendChatResult(json, request.room)

  }

}
