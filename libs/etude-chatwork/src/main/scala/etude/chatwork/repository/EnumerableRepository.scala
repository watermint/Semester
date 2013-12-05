package etude.chatwork.repository

import scala.util.Try
import etude.commons.domain.{Identity, Entity}

trait EnumerableRepository[ID <: Identity[_], E <: Entity[ID]] {
  def asEntitiesList: Try[List[E]]
}
