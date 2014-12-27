package etude.pintxos.chatwork.domain.infrastructure.api.v0.response

import etude.pintxos.chatwork.domain.model.message.{Message, MessageId}
import org.json4s.JValue

case class LoadOldChatResponse(rawResponse: JValue,
                               lastMessage: MessageId,
                               messages: Seq[Message])
  extends ChatWorkResponse
