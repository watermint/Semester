package etude.pintxos.chatwork.domain.infrastructure.api.v0.model

import etude.pintxos.chatwork.domain.model.message.Message

case class LoadChatResult(chatList: Seq[Message] = Seq(),
                          description: Option[String] = None,
                          publicDescription: Option[String] = None)