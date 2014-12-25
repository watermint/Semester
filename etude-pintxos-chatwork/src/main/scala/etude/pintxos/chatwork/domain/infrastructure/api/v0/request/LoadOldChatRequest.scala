package etude.pintxos.chatwork.domain.infrastructure.api.v0.request

import etude.pintxos.chatwork.domain.model.message.MessageId

case class LoadOldChatRequest(lastMessage: MessageId)
  extends ChatWorkRequest

