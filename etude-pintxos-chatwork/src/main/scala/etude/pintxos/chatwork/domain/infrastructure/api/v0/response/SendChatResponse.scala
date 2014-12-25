package etude.pintxos.chatwork.domain.infrastructure.api.v0.response

import etude.pintxos.chatwork.domain.infrastructure.api.v0.model.Storage
import etude.pintxos.chatwork.domain.model.message.Message


case class SendChatResponse(storage: Storage,
                            storageLimit: BigInt,
                            messages: Seq[Message])
