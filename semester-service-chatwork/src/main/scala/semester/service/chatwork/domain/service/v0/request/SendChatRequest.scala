package semester.service.chatwork.domain.service.v0.request

import semester.service.chatwork.domain.model.room.RoomId
import semester.service.chatwork.domain.service.v0.ChatWorkIOContext
import semester.service.chatwork.domain.service.v0.command.SendChat
import semester.service.chatwork.domain.service.v0.response.ChatWorkResponse

case class SendChatRequest(text: String,
                           room: RoomId)
  extends ChatWorkRequest {
  def execute(implicit context: ChatWorkIOContext): ChatWorkResponse = {
    SendChat.execute(this)
  }
}
