package etude.foundation.domain.lifecycle.sync

import etude.foundation.domain.model.{Entity, Identity}
import etude.foundation.domain.lifecycle.EntityReadableByStream
import scala.util.Try

trait SyncEntityReadableByStream[ID <: Identity[_], E <: Entity[ID]]
  extends SyncEntityIO
  with EntityReadableByStream[ID, E, Try] {

  type This <: SyncEntityReadableByStream[ID, E]
}
