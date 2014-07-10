package etude.domain.core.lifecycle.sync

import etude.domain.core.lifecycle.ResultWithIdentity
import etude.domain.core.model.{Entity, Identity}

import scala.util.Try

trait SyncResultWithIdentity[+R <: SyncEntityWriter[ID, E], ID <: Identity[_], E <: Entity[ID]]
  extends ResultWithIdentity[R, ID, E, Try]

object SyncResultWithIdentity {

}