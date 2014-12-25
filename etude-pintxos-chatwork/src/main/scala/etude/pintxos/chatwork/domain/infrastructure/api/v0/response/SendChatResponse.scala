package etude.pintxos.chatwork.domain.infrastructure.api.v0.response

import etude.pintxos.chatwork.domain.infrastructure.api.v0.model.Storage
import etude.pintxos.chatwork.domain.model.message.Message
import org.json4s.JValue


case class SendChatResponse(rawResponse: JValue,
                            storage: Storage,
                            storageLimit: BigInt,
                            messages: Seq[Message])
  extends ChatWorkResponse
