package etude.messaging.chatwork.domain.lifecycle.room

import scala.language.higherKinds
import etude.foundation.domain.lifecycle.Repository
import etude.messaging.chatwork.domain.model.room.{Participant, RoomId}

trait ParticipantRepository[M[+A]]
  extends Repository[RoomId, Participant, M] {

  type This <: ParticipantRepository[M]
}
