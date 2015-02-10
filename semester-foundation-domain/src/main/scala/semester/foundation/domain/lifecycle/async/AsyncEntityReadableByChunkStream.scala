package semester.foundation.domain.lifecycle.async

import semester.foundation.domain.lifecycle.EntityReadableByChunkStream
import semester.foundation.domain.model.{Entity, Identity}

import scala.concurrent.Future

trait AsyncEntityReadableByChunkStream[ID <: Identity[_], E <: Entity[ID]]
  extends AsyncEntityIO
  with EntityReadableByChunkStream[ID, E, Future] {

  type This <: AsyncEntityReadableByChunkStream[ID, E]
}
