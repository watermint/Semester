package etude.domain.core.lifecycle.async

import etude.domain.core.lifecycle.Repository
import etude.domain.core.model.{Entity, Identity}

import scala.concurrent.Future

trait AsyncRepository[ID <: Identity[_], E <: Entity[ID]]
  extends Repository[ID, E, Future]
  with AsyncEntityReader[ID, E]
  with AsyncEntityWriter[ID, E] {
  type This <: AsyncRepository[ID, E]
}