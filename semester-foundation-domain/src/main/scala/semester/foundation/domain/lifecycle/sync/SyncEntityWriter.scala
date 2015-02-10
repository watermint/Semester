package semester.foundation.domain.lifecycle.sync

import semester.foundation.domain.lifecycle.EntityWriter
import semester.foundation.domain.model.{Entity, Identity}

import scala.util.Try

trait SyncEntityWriter[ID <: Identity[_], E <: Entity[ID]]
  extends EntityWriter[ID, E, Try] {

  type This <: SyncEntityWriter[ID, E]
}
