package semester.service.chatwork.domain.service.request

import semester.service.chatwork.domain.model.message.MessageId
import semester.service.chatwork.domain.model.room.RoomId
import semester.service.chatwork.domain.service.ChatWorkIOContext
import semester.service.chatwork.domain.service.command.Read
import semester.service.chatwork.domain.service.response.ChatWorkResponse

case class ReadRequest(roomId: RoomId,
                       messageId: MessageId)
  extends ChatWorkRequest {
  def execute(implicit context: ChatWorkIOContext): ChatWorkResponse = {
    Read.execute(this)
  }
}
