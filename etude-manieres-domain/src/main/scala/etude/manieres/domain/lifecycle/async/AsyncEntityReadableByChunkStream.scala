package etude.manieres.domain.lifecycle.async

import etude.manieres.domain.lifecycle.EntityReadableByChunkStream
import etude.manieres.domain.model.{Entity, Identity}

import scala.concurrent.Future

trait AsyncEntityReadableByChunkStream[ID <: Identity[_], E <: Entity[ID]]
  extends AsyncEntityIO
  with EntityReadableByChunkStream[ID, E, Future] {

  type This <: AsyncEntityReadableByChunkStream[ID, E]
}
