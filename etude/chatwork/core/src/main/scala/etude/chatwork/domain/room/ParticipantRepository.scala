package etude.chatwork.domain.room

import scala.language.higherKinds
import etude.foundation.domain.lifecycle.Repository

trait ParticipantRepository[M[+A]]
  extends Repository[RoomId, Participant, M] {

  type This <: ParticipantRepository[M]
}
