package etude.messaging.chatwork.domain.room

import etude.foundation.domain.lifecycle.async.AsyncRepository
import scala.concurrent.Future

trait AsyncParticipantRepository
  extends ParticipantRepository[Future]
  with AsyncRepository[RoomId, Participant] {

  type This <: AsyncParticipantRepository
}