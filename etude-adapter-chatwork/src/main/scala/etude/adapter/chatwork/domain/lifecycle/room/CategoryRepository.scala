package etude.adapter.chatwork.domain.lifecycle.room

import etude.domain.core.lifecycle.{EntityIOContext, Repository, ResultWithIdentity}
import etude.adapter.chatwork.domain.model.room.{Category, CategoryId, RoomId}

import scala.language.higherKinds

trait CategoryRepository[M[+A]]
  extends Repository[CategoryId, Category, M] {

  type This <: CategoryRepository[M]

  def categories()(implicit context: EntityIOContext[M]): M[List[Category]]

  def create(name: String, rooms: List[RoomId])(implicit context: EntityIOContext[M]): M[ResultWithIdentity[This, CategoryId, Category, M]]
}