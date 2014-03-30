package etude.messaging.chatwork.domain.lifecycle.room

import scala.language.higherKinds
import etude.foundation.domain.lifecycle.EntityReader
import etude.messaging.chatwork.domain.model.room.{Room, RoomId}

private[room]
trait RoomReader[M[+A]]
  extends EntityReader[RoomId, Room, M] {

  type This <: RoomReader[M]
}
