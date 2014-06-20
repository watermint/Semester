package etude.domain.core.lifecycle.async

import etude.domain.core.lifecycle.EntityReadableByStream
import etude.domain.core.model.{Entity, Identity}

import scala.concurrent.Future

trait AsyncEntityReadableByStream[ID <: Identity[_], E <: Entity[ID]]
  extends AsyncEntityIO
  with EntityReadableByStream[ID, E, Future] {

  type This <: AsyncEntityReadableByStream[ID, E]
}
