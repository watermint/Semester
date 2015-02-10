package semester.service.chatwork.domain.service.response

import semester.service.chatwork.domain.service.request.ChatWorkRequest
import org.json4s.JsonAST.JValue

trait ChatWorkResponse {
  type Request <: ChatWorkRequest

  val rawResponse: JValue

  val request: Request
}