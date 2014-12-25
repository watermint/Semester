package etude.pintxos.chatwork.domain.lifecycle.room

import etude.manieres.domain.lifecycle.{EntityIOContext, Repository, ResultWithIdentity}
import etude.pintxos.chatwork.domain.model.room.{Category, CategoryId, RoomId}

import scala.language.higherKinds

trait CategoryRepository[M[+A]]
  extends Repository[CategoryId, Category, M] {

  type This <: CategoryRepository[M]

  def categories()(implicit context: EntityIOContext[M]): M[Seq[Category]]

  def create(name: String, rooms: List[RoomId])(implicit context: EntityIOContext[M]): M[ResultWithIdentity[This, CategoryId, Category, M]]
}
