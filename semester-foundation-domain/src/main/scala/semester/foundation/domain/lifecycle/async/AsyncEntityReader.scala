package semester.foundation.domain.lifecycle.async

import semester.foundation.domain.lifecycle.EntityReader
import semester.foundation.domain.model.{Entity, Identity}

import scala.concurrent.Future

trait AsyncEntityReader[ID <: Identity[_], E <: Entity[ID]]
  extends AsyncEntityIO
  with EntityReader[ID, E, Future] {

  type This <: AsyncEntityReader[ID, E]
}
