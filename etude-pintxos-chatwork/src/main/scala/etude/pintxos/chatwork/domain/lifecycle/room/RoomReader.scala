package etude.pintxos.chatwork.domain.lifecycle.room

import etude.manieres.domain.lifecycle.EntityReader
import etude.pintxos.chatwork.domain.model.room.{Room, RoomId}

import scala.language.higherKinds

private[room]
trait RoomReader[M[+A]]
  extends EntityReader[RoomId, Room, M] {

  type This <: RoomReader[M]
}
