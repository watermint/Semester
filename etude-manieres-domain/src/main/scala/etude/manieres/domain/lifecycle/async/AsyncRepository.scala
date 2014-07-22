package etude.manieres.domain.lifecycle.async

import etude.manieres.domain.lifecycle.Repository
import etude.manieres.domain.model.{Entity, Identity}

import scala.concurrent.Future

trait AsyncRepository[ID <: Identity[_], E <: Entity[ID]]
  extends Repository[ID, E, Future]
  with AsyncEntityReader[ID, E]
  with AsyncEntityWriter[ID, E] {
  type This <: AsyncRepository[ID, E]
}