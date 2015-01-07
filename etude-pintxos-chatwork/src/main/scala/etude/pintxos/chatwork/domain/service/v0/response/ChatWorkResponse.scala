package etude.pintxos.chatwork.domain.service.v0.response

import org.json4s.JsonAST.JValue

trait ChatWorkResponse {
  val rawResponse: JValue
}