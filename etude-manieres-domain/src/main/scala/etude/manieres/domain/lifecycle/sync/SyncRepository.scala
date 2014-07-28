package etude.manieres.domain.lifecycle.sync

import etude.manieres.domain.lifecycle.Repository
import etude.manieres.domain.model.{Identity, Entity}

import scala.util.Try

trait SyncRepository[ID <: Identity[_], E <: Entity[ID]]
  extends Repository[ID, E, Try]
  with SyncEntityReader[ID, E]
  with SyncEntityWriter[ID, E] {
  type This <: SyncRepository[ID, E]
}
