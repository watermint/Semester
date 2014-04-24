package etude.messaging.chatwork.domain.model.room

import scala.language.higherKinds
import etude.domain.core.lifecycle.{EntityIOContext, Factory}

trait RoomFactory[M[+A]]
  extends Factory {

  def create(name: String,
             description: String = "",
             icon: RoomIcon = RoomIconGroup())
            (implicit context: EntityIOContext[M]): M[RoomId]
}
