package etude.pintxos.chatwork.domain.infrastructure.api.v0.command

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.infrastructure.api.v0.V0AsyncApi._
import etude.pintxos.chatwork.domain.infrastructure.api.v0.model.ReadResult
import etude.pintxos.chatwork.domain.infrastructure.api.v0.{V0AsyncEntityIO, V0AsyncApi}
import etude.pintxos.chatwork.domain.model.message.MessageId
import etude.pintxos.chatwork.domain.model.room.RoomId
import org.json4s._

import scala.concurrent.Future

object Read extends V0AsyncEntityIO {

  private def parseRead(json: JValue): ReadResult = {
    val results: List[ReadResult] = for {
      JObject(j) <- json
      JField("read_num", JInt(readNum)) <- j
      JField("mention_Num", JInt(mentionNum)) <- j
    } yield {
      ReadResult(readNum, mentionNum)
    }
    results.last
  }

  def read(roomId: RoomId, messageId: MessageId)
          (implicit context: EntityIOContext[Future]): Future[ReadResult] = {
    implicit val executor = getExecutionContext(context)

    V0AsyncApi.api(
      "read",
      Map(
        "room_id" -> roomId.value.toString(),
        "last_chat_id" -> messageId.toString
      )
    ) map {
      json =>
        parseRead(json)
    }
  }
}
