package etude.foundation.domain.lifecycle.sync

import etude.foundation.domain.model.{Entity, Identity}
import etude.foundation.domain.lifecycle.EntityWriter
import scala.util.Try

trait SyncEntityWriter[ID <: Identity[_], E <: Entity[ID]] extends EntityWriter[ID, E, Try] {
  type This <: SyncEntityWriter[ID, E]

  def store(entity: E)(implicit context: SyncEntityIOContext): Try[SyncResultWithEntity[This, ID, E]]
}
