package etude.foundation.domain.lifecycle.sync

import etude.foundation.domain.model.{Identity, Entity}
import etude.foundation.domain.lifecycle.EntityReader
import scala.util.Try

trait SyncEntityReader[ID <: Identity[_], E <: Entity[ID]] extends EntityReader[ID, E, Try] {
  def resolve(identity: ID)(implicit context: SyncEntityIOContext): Try[E]

}
