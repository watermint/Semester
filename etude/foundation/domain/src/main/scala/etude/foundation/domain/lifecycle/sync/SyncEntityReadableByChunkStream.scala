package etude.foundation.domain.lifecycle.sync

import etude.foundation.domain.lifecycle.EntityReadableByChunkStream
import etude.foundation.domain.model.{Entity, Identity}
import scala.util.Try

trait SyncEntityReadableByChunkStream[ID <: Identity[_], E <: Entity[ID]]
  extends SyncEntityIO
  with EntityReadableByChunkStream[ID, E, Try] {
  type This <: SyncEntityReadableByChunkStream[ID, E]
}
