package semester.foundation.domain.lifecycle.sync

import semester.foundation.domain.lifecycle.EntityReadableByStream
import semester.foundation.domain.model.{Entity, Identity}

import scala.util.Try

trait SyncEntityReadableByStream[ID <: Identity[_], E <: Entity[ID]]
  extends SyncEntityIO
  with EntityReadableByStream[ID, E, Try] {

  type This <: SyncEntityReadableByStream[ID, E]
}
