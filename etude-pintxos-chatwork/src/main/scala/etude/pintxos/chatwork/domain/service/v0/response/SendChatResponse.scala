package etude.pintxos.chatwork.domain.service.v0.response

import etude.pintxos.chatwork.domain.model.message.Message
import etude.pintxos.chatwork.domain.service.v0.model.Storage
import org.json4s.JValue


case class SendChatResponse(rawResponse: JValue,
                            storage: Storage,
                            storageLimit: BigInt,
                            messages: Seq[Message])
  extends ChatWorkResponse
