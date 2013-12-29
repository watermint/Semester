package etude.chatwork.domain.room

import scala.language.higherKinds
import etude.foundation.domain.lifecycle.EntityIOContext

trait RoomRepository[M[+A]]
  extends RoomReader[M] {

  def myRoom()(implicit context: EntityIOContext[M]): M[Room]
}
