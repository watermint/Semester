package etude.chatwork.v1

import scala.util.Try

trait EnumerableRepository[K <: Id[K, T], T <: Entity[K]] {
  def asEntitiesList: Try[List[T]]
}
