package etude.chatwork.domain.room

import scala.language.higherKinds
import etude.foundation.domain.lifecycle.{EntityIOContext, Factory}

trait RoomFactory[M[+A]]
  extends Factory {

  def create(name: String,
             description: String = "",
             icon: RoomIcon = RoomIconGroup())
            (implicit context: EntityIOContext[M]): M[RoomId]
}
