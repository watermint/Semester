package etude.pintxos.chatwork.domain.infrastructure.api.v0.command

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.infrastructure.api.v0.V0AsyncApi._
import etude.pintxos.chatwork.domain.infrastructure.api.v0.{V0AsyncEntityIO, V0AsyncApi}
import etude.pintxos.chatwork.domain.infrastructure.api.v0.model.{SendChatResult, Storage}
import etude.pintxos.chatwork.domain.infrastructure.api.v0.parser.{MessageParser, StorageParser}
import etude.pintxos.chatwork.domain.model.message.Message
import etude.pintxos.chatwork.domain.model.room.RoomId
import org.json4s._

import scala.concurrent.Future

object SendChat extends V0AsyncEntityIO {

  def parseSendChatResult(json: JValue, room: RoomId): SendChatResult = {
    val results: List[SendChatResult] = for {
      JObject(j) <- json
      JField("storage", storage) <- j
      JField("storage_limit", JString(storageLimit)) <- j
    } yield {
      j.toMap.get("chat_list") match {
        case Some(chatList) =>
          SendChatResult(
            StorageParser.parseStorage(storage),
            BigInt(storageLimit),
            MessageParser.parseMessage(room, chatList)
          )
        case _ =>
          SendChatResult(
            StorageParser.parseStorage(storage),
            BigInt(storageLimit),
            Seq()
          )
      }
    }
    results.last
  }

  def sendChat(text: String, room: RoomId)
              (implicit context: EntityIOContext[Future]): Future[SendChatResult] = {
    implicit val executor = getExecutionContext(context)
    import org.json4s.JsonDSL._
    import org.json4s.native.JsonMethods._

    val pdata = ("text" -> text) ~
      ("room_id" -> room.value.toString()) ~
      ("edit_id" -> "0")

    V0AsyncApi.api(
      "send_chat",
      Map.empty,
      Map(
        "pdata" -> compact(render(pdata))
      )
    ) map {
      json =>
        parseSendChatResult(json, room)
    }
  }
}
