package etude.pintxos.chatwork.domain.service.v0.request

import etude.pintxos.chatwork.domain.model.message.MessageId
import etude.pintxos.chatwork.domain.service.v0.ChatWorkIOContext
import etude.pintxos.chatwork.domain.service.v0.command.LoadOldChat
import etude.pintxos.chatwork.domain.service.v0.response.ChatWorkResponse

case class LoadOldChatRequest(lastMessage: MessageId)
  extends ChatWorkRequest {
  def execute(implicit context: ChatWorkIOContext): ChatWorkResponse[_] = {
    LoadOldChat.execute(this)
  }
}

