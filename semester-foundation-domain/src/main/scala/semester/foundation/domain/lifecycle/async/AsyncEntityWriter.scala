package semester.foundation.domain.lifecycle.async

import semester.foundation.domain.lifecycle.EntityWriter
import semester.foundation.domain.model.{Entity, Identity}

import scala.concurrent.Future

trait AsyncEntityWriter[ID <: Identity[_], E <: Entity[ID]]
  extends AsyncEntityIO
  with EntityWriter[ID, E, Future] {

  type This <: AsyncEntityWriter[ID, E]
}
