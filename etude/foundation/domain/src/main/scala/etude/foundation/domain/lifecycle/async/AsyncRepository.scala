package etude.foundation.domain.lifecycle.async

import etude.foundation.domain.model.{Entity, Identity}
import etude.foundation.domain.lifecycle.Repository
import scala.concurrent.Future

trait AsyncRepository[ID <: Identity[_], E <: Entity[ID]]
  extends Repository[ID, E, Future]
  with AsyncEntityReader[ID, E]
  with AsyncEntityWriter[ID, E] {
  type This <: AsyncRepository[ID, E]
}