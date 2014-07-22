package etude.manieres.domain.lifecycle.async

import etude.manieres.domain.lifecycle.EntityReadableByStream
import etude.manieres.domain.model.{Entity, Identity}

import scala.concurrent.Future

trait AsyncEntityReadableByStream[ID <: Identity[_], E <: Entity[ID]]
  extends AsyncEntityIO
  with EntityReadableByStream[ID, E, Future] {

  type This <: AsyncEntityReadableByStream[ID, E]
}
