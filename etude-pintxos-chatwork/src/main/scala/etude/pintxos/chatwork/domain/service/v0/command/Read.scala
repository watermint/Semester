package etude.pintxos.chatwork.domain.service.v0.command

import etude.pintxos.chatwork.domain.service.v0.request.ReadRequest
import etude.pintxos.chatwork.domain.service.v0.response.ReadResponse
import etude.pintxos.chatwork.domain.service.v0.{ChatWorkApi, ChatWorkIOContext}
import org.json4s._

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

  def execute(request: ReadRequest)(implicit context: ChatWorkIOContext): ReadResponse = {

    val json = ChatWorkApi.api(
      "read",
      Map(
        "room_id" -> request.roomId.value.toString(),
        "last_chat_id" -> request.messageId.messageId.toString()
      )
    )

    parseRead(json)
  }

}
