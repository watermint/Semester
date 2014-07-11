package etude.adapter.chatwork.domain.model.room

import etude.domain.core.lifecycle.{EntityIOContext, Factory}

import scala.language.higherKinds

trait RoomFactory[M[+A]]
  extends Factory {

  def create(name: String,
             description: String = "",
             icon: RoomIcon = RoomIconGroup())
            (implicit context: EntityIOContext[M]): M[RoomId]
}
