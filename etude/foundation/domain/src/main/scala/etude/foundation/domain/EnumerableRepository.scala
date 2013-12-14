package etude.foundation.domain

import scala.util.Try

trait EnumerableRepository[ID <: Identity[_], E <: Entity[ID]] {
  def asEntitiesList: Try[List[E]]
}