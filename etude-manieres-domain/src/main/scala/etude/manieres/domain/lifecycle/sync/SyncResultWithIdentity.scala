package etude.manieres.domain.lifecycle.sync

import etude.manieres.domain.lifecycle.ResultWithIdentity
import etude.manieres.domain.model.{Entity, Identity}

import scala.util.Try

trait SyncResultWithIdentity[+R <: SyncEntityWriter[ID, E], ID <: Identity[_], E <: Entity[ID]]
  extends ResultWithIdentity[R, ID, E, Try]

object SyncResultWithIdentity {

}