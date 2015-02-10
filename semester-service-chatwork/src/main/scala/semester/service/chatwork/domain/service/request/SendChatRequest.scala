package semester.service.chatwork.domain.service.request

import semester.service.chatwork.domain.model.room.RoomId
import semester.service.chatwork.domain.service.ChatWorkIOContext
import semester.service.chatwork.domain.service.command.SendChat
import semester.service.chatwork.domain.service.response.ChatWorkResponse

case class SendChatRequest(text: String,
                           room: RoomId)
  extends ChatWorkRequest {
  def execute(implicit context: ChatWorkIOContext): ChatWorkResponse = {
    SendChat.execute(this)
  }
}
