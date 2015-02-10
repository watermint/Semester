package semester.foundation.domain.lifecycle.sync

import semester.foundation.domain.lifecycle.ResultWithIdentity
import semester.foundation.domain.model.{Entity, Identity}

import scala.util.Try

trait SyncResultWithIdentity[+R <: SyncEntityWriter[ID, E], ID <: Identity[_], E <: Entity[ID]]
  extends ResultWithIdentity[R, ID, E, Try]

object SyncResultWithIdentity {

}