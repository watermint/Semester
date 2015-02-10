package semester.foundation.domain.lifecycle.async

import semester.foundation.domain.lifecycle.Repository
import semester.foundation.domain.model.{Entity, Identity}

import scala.concurrent.Future

trait AsyncRepository[ID <: Identity[_], E <: Entity[ID]]
  extends Repository[ID, E, Future]
  with AsyncEntityReader[ID, E]
  with AsyncEntityWriter[ID, E] {
  type This <: AsyncRepository[ID, E]
}