package etude.pintxos.chatwork.domain.service.v0.response

import etude.pintxos.chatwork.domain.service.v0.request.ChatWorkRequest
import org.json4s.JsonAST.JValue

trait ChatWorkResponse {
  type Request <: ChatWorkRequest

  val rawResponse: JValue

  val request: Request
}