package etude.pintxos.chatwork.domain.service.v0.command

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.service.v0.Api
import etude.pintxos.chatwork.domain.service.v0.request.ReadRequest
import etude.pintxos.chatwork.domain.service.v0.response.ReadResponse
import org.json4s._

import scala.concurrent.Future

object Read
  extends ChatWorkCommand[ReadRequest, ReadResponse] {

  private def parseRead(json: JValue): ReadResponse = {
    val results: List[ReadResponse] = for {
      JObject(j) <- json
      JField("read_num", JInt(readNum)) <- j
      JField("mention_num", JInt(mentionNum)) <- j
    } yield {
      ReadResponse(json, readNum, mentionNum)
    }
    results.last
  }

  def execute(request: ReadRequest)(implicit context: EntityIOContext[Future]): ReadResponse = {
    implicit val executor = getExecutionContext(context)

    val json = Api.api(
      "read",
      Map(
        "room_id" -> request.roomId.value.toString(),
        "last_chat_id" -> request.messageId.messageId.toString()
      )
    )

    parseRead(json)
  }

}
