package semester.foundation.domain.lifecycle.sync

import semester.foundation.domain.lifecycle.EntityReader
import semester.foundation.domain.model.{Entity, Identity}

import scala.util.Try

trait SyncEntityReader[ID <: Identity[_], E <: Entity[ID]]
  extends EntityReader[ID, E, Try] {

  type This <: SyncEntityReader[ID, E]

}
