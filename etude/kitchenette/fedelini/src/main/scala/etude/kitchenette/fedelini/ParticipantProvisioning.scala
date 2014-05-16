package etude.kitchenette.fedelini

import etude.messaging.chatwork.domain.model.room.{Participant, RoomId}
import scala.concurrent.Future
import etude.domain.core.lifecycle.EntityIOContext
import etude.messaging.chatwork.domain.lifecycle.room.AsyncParticipantRepository

class ParticipantProvisioning {
  val participantRepository = AsyncParticipantRepository.ofV0Api()

  def appendParticipants(toRoom: RoomId, fromRoom: RoomId)(implicit context: EntityIOContext[Future]): Future[Participant] = {
    ???
  }
}
