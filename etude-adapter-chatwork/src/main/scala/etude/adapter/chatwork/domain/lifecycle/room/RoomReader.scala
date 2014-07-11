package etude.adapter.chatwork.domain.lifecycle.room

import etude.domain.core.lifecycle.EntityReader
import etude.adapter.chatwork.domain.model.room.{Room, RoomId}

import scala.language.higherKinds

private[room]
trait RoomReader[M[+A]]
  extends EntityReader[RoomId, Room, M] {

  type This <: RoomReader[M]
}
