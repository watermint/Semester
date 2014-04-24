package etude.domain.core.lifecycle.async

import etude.domain.core.model.{Entity, Identity}
import etude.domain.core.lifecycle.EntityReadableByChunkStream
import scala.concurrent.Future

trait AsyncEntityReadableByChunkStream[ID <: Identity[_], E <: Entity[ID]]
  extends AsyncEntityIO
  with EntityReadableByChunkStream[ID, E, Future] {

  type This <: AsyncEntityReadableByChunkStream[ID, E]
}
