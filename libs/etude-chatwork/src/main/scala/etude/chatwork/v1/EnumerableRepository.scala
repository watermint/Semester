package etude.chatwork.v1

import scala.util.Try
import etude.ddd.model.{Entity, Identity}

trait EnumerableRepository[ID <: Identity[_], E <: Entity[_]] {
  def asEntitiesList: Try[List[E]]
}
