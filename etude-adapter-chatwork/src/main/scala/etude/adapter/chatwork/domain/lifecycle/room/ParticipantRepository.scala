package etude.adapter.chatwork.domain.lifecycle.room

import etude.domain.core.lifecycle.Repository
import etude.adapter.chatwork.domain.model.room.{Participant, RoomId}

import scala.language.higherKinds

trait ParticipantRepository[M[+A]]
  extends Repository[RoomId, Participant, M] {

  type This <: ParticipantRepository[M]
}
