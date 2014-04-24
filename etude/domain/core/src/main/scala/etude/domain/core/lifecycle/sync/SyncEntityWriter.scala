package etude.domain.core.lifecycle.sync

import etude.domain.core.model.{Entity, Identity}
import etude.domain.core.lifecycle.EntityWriter
import scala.util.Try

trait SyncEntityWriter[ID <: Identity[_], E <: Entity[ID]]
  extends EntityWriter[ID, E, Try] {

  type This <: SyncEntityWriter[ID, E]
}
