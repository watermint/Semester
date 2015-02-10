package semester.foundation.domain.lifecycle.sync

import semester.foundation.domain.lifecycle.EntityReadableByChunkStream
import semester.foundation.domain.model.{Entity, Identity}

import scala.util.Try

trait SyncEntityReadableByChunkStream[ID <: Identity[_], E <: Entity[ID]]
  extends SyncEntityIO
  with EntityReadableByChunkStream[ID, E, Try] {
  type This <: SyncEntityReadableByChunkStream[ID, E]
}
