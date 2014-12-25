package etude.pintxos.chatwork.domain.infrastructure.api.v0.response

import etude.pintxos.chatwork.domain.model.message.Message
import org.json4s.JValue

case class LoadOldChatResponse(rawResponse: JValue,
                               messages: Seq[Message])
  extends ChatWorkResponse
