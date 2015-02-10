package semester.service.chatwork.domain.service.v0.request

import semester.service.chatwork.domain.model.room.Participant
import semester.service.chatwork.domain.service.v0.ChatWorkIOContext
import semester.service.chatwork.domain.service.v0.command.UpdateRoom
import semester.service.chatwork.domain.service.v0.response.ChatWorkResponse

case class UpdateRoomRequest(participant: Participant)
  extends ChatWorkRequest {
  def execute(implicit context: ChatWorkIOContext): ChatWorkResponse = {
    UpdateRoom.execute(this)
  }
}
