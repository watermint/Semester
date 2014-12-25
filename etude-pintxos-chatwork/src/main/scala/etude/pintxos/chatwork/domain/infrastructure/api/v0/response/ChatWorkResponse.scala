package etude.pintxos.chatwork.domain.infrastructure.api.v0.response

import org.json4s.JsonAST.JValue

trait ChatWorkResponse {
  val rawResponse: JValue
}