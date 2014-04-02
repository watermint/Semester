package etude.messaging.chatwork.domain.lifecycle.room

import scala.language.higherKinds
import etude.foundation.domain.lifecycle.{ResultWithEntity, EntityIOContext, Repository}
import etude.messaging.chatwork.domain.model.room.{RoomId, Category, CategoryId}

trait CategoryRepository[M[+A]]
  extends Repository[CategoryId, Category, M] {

  type This <: CategoryRepository[M]

  def categories()(implicit context: EntityIOContext[M]): M[List[Category]]

  def create(name: String, rooms: List[RoomId])(implicit context: EntityIOContext[M]): M[ResultWithEntity[This, CategoryId, Category, M]]
}
