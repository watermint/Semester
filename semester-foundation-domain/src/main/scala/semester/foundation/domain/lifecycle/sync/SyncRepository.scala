package semester.foundation.domain.lifecycle.sync

import semester.foundation.domain.lifecycle.Repository
import semester.foundation.domain.model.{Entity, Identity}

import scala.util.Try

trait SyncRepository[ID <: Identity[_], E <: Entity[ID]]
  extends Repository[ID, E, Try]
  with SyncEntityReader[ID, E]
  with SyncEntityWriter[ID, E] {
  type This <: SyncRepository[ID, E]
}
