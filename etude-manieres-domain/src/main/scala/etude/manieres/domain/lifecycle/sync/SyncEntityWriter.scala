package etude.manieres.domain.lifecycle.sync

import etude.manieres.domain.lifecycle.EntityWriter
import etude.manieres.domain.model.{Entity, Identity}

import scala.util.Try

trait SyncEntityWriter[ID <: Identity[_], E <: Entity[ID]]
  extends EntityWriter[ID, E, Try] {

  type This <: SyncEntityWriter[ID, E]
}
