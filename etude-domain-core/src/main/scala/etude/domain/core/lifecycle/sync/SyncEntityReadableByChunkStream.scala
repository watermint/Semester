package etude.domain.core.lifecycle.sync

import etude.domain.core.lifecycle.EntityReadableByChunkStream
import etude.domain.core.model.{Entity, Identity}

import scala.util.Try

trait SyncEntityReadableByChunkStream[ID <: Identity[_], E <: Entity[ID]]
  extends SyncEntityIO
  with EntityReadableByChunkStream[ID, E, Try] {
  type This <: SyncEntityReadableByChunkStream[ID, E]
}
