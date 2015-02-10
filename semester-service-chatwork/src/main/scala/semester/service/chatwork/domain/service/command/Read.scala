package semester.service.chatwork.domain.service.command

import org.json4s._
import semester.service.chatwork.domain.service.request.ReadRequest
import semester.service.chatwork.domain.service.response.ReadResponse
import semester.service.chatwork.domain.service.{ChatWorkApi, ChatWorkIOContext}

object Read
  extends ChatWorkCommand[ReadRequest, ReadResponse] {

  private def parseRead(json: JValue, request: ReadRequest): ReadResponse = {
    val results: List[ReadResponse] = for {
      JObject(j) <- json
      JField("read_num", JInt(readNum)) <- j
      JField("mention_num", JInt(mentionNum)) <- j
    } yield {
      ReadResponse(json, request, readNum, mentionNum)
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

    parseRead(json, request)
  }

}
