package etude.manieres.domain.lifecycle.async

import etude.manieres.domain.lifecycle.EntityReader
import etude.manieres.domain.model.{Entity, Identity}

import scala.concurrent.Future

trait AsyncEntityReader[ID <: Identity[_], E <: Entity[ID]]
  extends AsyncEntityIO
  with EntityReader[ID, E, Future] {

  type This <: AsyncEntityReader[ID, E]
}
