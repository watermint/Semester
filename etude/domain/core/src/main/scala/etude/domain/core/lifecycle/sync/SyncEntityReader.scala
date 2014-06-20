package etude.domain.core.lifecycle.sync

import etude.domain.core.lifecycle.EntityReader
import etude.domain.core.model.{Entity, Identity}

import scala.util.Try

trait SyncEntityReader[ID <: Identity[_], E <: Entity[ID]]
  extends EntityReader[ID, E, Try] {

  type This <: SyncEntityReader[ID, E]

}
