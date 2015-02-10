package semester.service.chatwork.domain.service.v0.response

import semester.service.chatwork.domain.service.v0.request.ChatWorkRequest
import org.json4s.JsonAST.JValue

trait ChatWorkResponse {
  type Request <: ChatWorkRequest

  val rawResponse: JValue

  val request: Request
}