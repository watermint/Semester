package etude.domain.core.lifecycle.async

import etude.domain.core.lifecycle.EntityWriter
import etude.domain.core.model.{Entity, Identity}

import scala.concurrent.Future

trait AsyncEntityWriter[ID <: Identity[_], E <: Entity[ID]]
  extends AsyncEntityIO
  with EntityWriter[ID, E, Future] {

  type This <: AsyncEntityWriter[ID, E]
}
