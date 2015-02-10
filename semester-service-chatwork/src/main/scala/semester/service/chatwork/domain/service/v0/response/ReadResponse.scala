package semester.service.chatwork.domain.service.v0.response

import semester.service.chatwork.domain.service.v0.request.ReadRequest
import org.json4s.JValue

case class ReadResponse(rawResponse: JValue,
                        request: ReadRequest,
                        readNum: BigInt,
                        mentionNum: BigInt)
  extends ChatWorkResponse {
  type Request = ReadRequest
}
