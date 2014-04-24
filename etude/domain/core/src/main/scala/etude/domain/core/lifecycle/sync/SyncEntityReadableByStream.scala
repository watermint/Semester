package etude.domain.core.lifecycle.sync

import etude.domain.core.model.{Entity, Identity}
import etude.domain.core.lifecycle.EntityReadableByStream
import scala.util.Try

trait SyncEntityReadableByStream[ID <: Identity[_], E <: Entity[ID]]
  extends SyncEntityIO
  with EntityReadableByStream[ID, E, Try] {

  type This <: SyncEntityReadableByStream[ID, E]
}
