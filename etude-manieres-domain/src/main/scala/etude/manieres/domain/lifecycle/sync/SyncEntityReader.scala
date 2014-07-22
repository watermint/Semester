package etude.manieres.domain.lifecycle.sync

import etude.manieres.domain.lifecycle.EntityReader
import etude.manieres.domain.model.{Entity, Identity}

import scala.util.Try

trait SyncEntityReader[ID <: Identity[_], E <: Entity[ID]]
  extends EntityReader[ID, E, Try] {

  type This <: SyncEntityReader[ID, E]

}
