package etude.pintxos.chatwork.domain.infrastructure.api.v0.request

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.infrastructure.api.v0.command.UpdateRoom
import etude.pintxos.chatwork.domain.infrastructure.api.v0.response.{ChatWorkResponse, UpdateRoomResponse}
import etude.pintxos.chatwork.domain.model.room.Participant

import scala.concurrent.Future

case class UpdateRoomRequest(participant: Participant)
  extends ChatWorkRequest {
  def execute(implicit context: EntityIOContext[Future]): Future[ChatWorkResponse] = {
    UpdateRoom.execute(this)
  }
}
