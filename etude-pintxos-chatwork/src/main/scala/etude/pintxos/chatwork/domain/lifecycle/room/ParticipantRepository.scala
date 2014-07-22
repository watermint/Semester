package etude.pintxos.chatwork.domain.lifecycle.room

import etude.manieres.domain.lifecycle.Repository
import etude.pintxos.chatwork.domain.model.room.{Participant, RoomId}

import scala.language.higherKinds

trait ParticipantRepository[M[+A]]
  extends Repository[RoomId, Participant, M] {

  type This <: ParticipantRepository[M]
}
