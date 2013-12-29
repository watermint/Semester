package etude.foundation.domain.lifecycle.async

import etude.foundation.domain.model.{Entity, Identity}
import etude.foundation.domain.lifecycle.EntityReadableByStream
import scala.concurrent.Future

trait AsyncEntityReadableByStream[ID <: Identity[_], E <: Entity[ID]]
  extends AsyncEntityIO
  with EntityReadableByStream[ID, E, Future] {

  type This <: AsyncEntityReadableByStream[ID, E]
}
