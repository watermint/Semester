package etude.manieres.domain.lifecycle.async

import etude.manieres.domain.lifecycle.EntityWriter
import etude.manieres.domain.model.{Entity, Identity}

import scala.concurrent.Future

trait AsyncEntityWriter[ID <: Identity[_], E <: Entity[ID]]
  extends AsyncEntityIO
  with EntityWriter[ID, E, Future] {

  type This <: AsyncEntityWriter[ID, E]
}
