package semester.service.chatwork.domain.service.v0.request

import semester.service.chatwork.domain.model.message.MessageId
import semester.service.chatwork.domain.model.room.RoomId
import semester.service.chatwork.domain.service.v0.ChatWorkIOContext
import semester.service.chatwork.domain.service.v0.command.Read
import semester.service.chatwork.domain.service.v0.response.ChatWorkResponse

case class ReadRequest(roomId: RoomId,
                       messageId: MessageId)
  extends ChatWorkRequest {
  def execute(implicit context: ChatWorkIOContext): ChatWorkResponse = {
    Read.execute(this)
  }
}
