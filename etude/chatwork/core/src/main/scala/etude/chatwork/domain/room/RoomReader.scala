package etude.chatwork.domain.room

import scala.language.higherKinds
import etude.foundation.domain.lifecycle.EntityReader

trait RoomReader[M[+A]]
  extends EntityReader[RoomId, Room, M] {

  type This <: RoomReader[M]
}
