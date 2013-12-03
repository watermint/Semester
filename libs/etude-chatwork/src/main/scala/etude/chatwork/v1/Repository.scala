package etude.chatwork.v1

import scala.util.Try

trait Repository[K <: Id[K, T], T <: Entity[K]] {
  def resolve(identifier: K): Try[T]

  def asEntitiesList: Try[List[T]]

  def contains(identifier: K): Try[Boolean]

  def contains(entity: T): Try[Boolean]
}
