package etude.foundation.domain.lifecycle.async

import etude.foundation.domain.model.{Entity, Identity}
import etude.foundation.domain.lifecycle.EntityReadableByChunkStream
import scala.concurrent.Future

trait AsyncEntityReadableByChunkStream[ID <: Identity[_], E <: Entity[ID]]
  extends AsyncEntityIO
  with EntityReadableByChunkStream[ID, E, Future] {

  type This <: AsyncEntityReadableByChunkStream[ID, E]
}
