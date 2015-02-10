package semester.service.chatwork.domain.service.v0.request

import semester.service.chatwork.domain.model.message.MessageId
import semester.service.chatwork.domain.model.room.RoomId
import semester.service.chatwork.domain.service.v0.ChatWorkIOContext
import semester.service.chatwork.domain.service.v0.command.LoadChat
import semester.service.chatwork.domain.service.v0.response.ChatWorkResponse

case class LoadChatRequest(room: RoomId,
                           firstChatId: Option[MessageId] = None,
                           lastChatId: Option[MessageId] = None,
                           jumpToChatId: Option[MessageId] = None,
                           unreadNum: Boolean = false,
                           description: Boolean = false,
                           task: Boolean = false)
  extends ChatWorkRequest {
  def execute(implicit context: ChatWorkIOContext): ChatWorkResponse = {
    LoadChat.execute(this)
  }
}
