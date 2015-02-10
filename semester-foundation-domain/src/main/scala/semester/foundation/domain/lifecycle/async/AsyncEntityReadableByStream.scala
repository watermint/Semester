package semester.foundation.domain.lifecycle.async

import semester.foundation.domain.lifecycle.EntityReadableByStream
import semester.foundation.domain.model.{Entity, Identity}

import scala.concurrent.Future

trait AsyncEntityReadableByStream[ID <: Identity[_], E <: Entity[ID]]
  extends AsyncEntityIO
  with EntityReadableByStream[ID, E, Future] {

  type This <: AsyncEntityReadableByStream[ID, E]
}
