package etude.pintxos.chatwork.domain.service.v0.request

import etude.pintxos.chatwork.domain.model.room.Participant
import etude.pintxos.chatwork.domain.service.v0.ChatWorkIOContext
import etude.pintxos.chatwork.domain.service.v0.command.UpdateRoom
import etude.pintxos.chatwork.domain.service.v0.response.ChatWorkResponse

case class UpdateRoomRequest(participant: Participant)
  extends ChatWorkRequest {
  def execute(implicit context: ChatWorkIOContext): ChatWorkResponse = {
    UpdateRoom.execute(this)
  }
}
