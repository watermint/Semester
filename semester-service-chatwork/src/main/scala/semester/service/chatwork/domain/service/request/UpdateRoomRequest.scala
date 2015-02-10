package semester.service.chatwork.domain.service.request

import semester.service.chatwork.domain.model.room.Participant
import semester.service.chatwork.domain.service.ChatWorkIOContext
import semester.service.chatwork.domain.service.command.UpdateRoom
import semester.service.chatwork.domain.service.response.ChatWorkResponse

case class UpdateRoomRequest(participant: Participant)
  extends ChatWorkRequest {
  def execute(implicit context: ChatWorkIOContext): ChatWorkResponse = {
    UpdateRoom.execute(this)
  }
}
