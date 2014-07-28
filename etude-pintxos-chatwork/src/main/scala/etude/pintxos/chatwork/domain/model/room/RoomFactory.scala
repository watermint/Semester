package etude.pintxos.chatwork.domain.model.room

import etude.manieres.domain.lifecycle.EntityIOContext

import scala.language.higherKinds

trait RoomFactory[M[+A]] {

  def create(name: String,
             description: String = "",
             icon: RoomIcon = RoomIconGroup())
            (implicit context: EntityIOContext[M]): M[RoomId]
}
