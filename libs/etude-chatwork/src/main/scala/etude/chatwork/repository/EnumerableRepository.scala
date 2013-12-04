package etude.chatwork.repository

import scala.util.Try
import etude.ddd.model.{Entity, Identity}

trait EnumerableRepository[ID <: Identity[_], E <: Entity[ID]] {
  def asEntitiesList: Try[List[E]]
}
