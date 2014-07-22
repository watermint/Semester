package etude.manieres.domain.lifecycle.sync

import etude.manieres.domain.lifecycle.EntityReadableByChunkStream
import etude.manieres.domain.model.{Entity, Identity}

import scala.util.Try

trait SyncEntityReadableByChunkStream[ID <: Identity[_], E <: Entity[ID]]
  extends SyncEntityIO
  with EntityReadableByChunkStream[ID, E, Try] {
  type This <: SyncEntityReadableByChunkStream[ID, E]
}
