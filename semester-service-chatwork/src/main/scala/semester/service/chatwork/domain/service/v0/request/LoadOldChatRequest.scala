package semester.service.chatwork.domain.service.v0.request

import semester.service.chatwork.domain.model.message.MessageId
import semester.service.chatwork.domain.service.v0.ChatWorkIOContext
import semester.service.chatwork.domain.service.v0.command.LoadOldChat
import semester.service.chatwork.domain.service.v0.response.ChatWorkResponse

case class LoadOldChatRequest(lastMessage: MessageId)
  extends ChatWorkRequest {
  def execute(implicit context: ChatWorkIOContext): ChatWorkResponse = {
    LoadOldChat.execute(this)
  }
}

