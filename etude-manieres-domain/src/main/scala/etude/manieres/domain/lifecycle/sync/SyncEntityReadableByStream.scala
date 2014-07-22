package etude.manieres.domain.lifecycle.sync

import etude.manieres.domain.lifecycle.EntityReadableByStream
import etude.manieres.domain.model.{Entity, Identity}

import scala.util.Try

trait SyncEntityReadableByStream[ID <: Identity[_], E <: Entity[ID]]
  extends SyncEntityIO
  with EntityReadableByStream[ID, E, Try] {

  type This <: SyncEntityReadableByStream[ID, E]
}
