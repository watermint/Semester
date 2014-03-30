package etude.messaging.chatwork.domain.lifecycle.room

import etude.foundation.domain.lifecycle.async.AsyncRepository
import scala.concurrent.Future
import etude.messaging.chatwork.domain.model.room.{Participant, RoomId}

trait AsyncParticipantRepository
  extends ParticipantRepository[Future]
  with AsyncRepository[RoomId, Participant] {

  type This <: AsyncParticipantRepository
}

object AsyncParticipantRepository {
  def ofV0Api(): AsyncParticipantRepository =
    new AsyncParticipantRepositoryOnV0Api

  def ofV1Api(): AsyncParticipantRepository =
    new AsyncParticipantRepositoryOnV1Api
}