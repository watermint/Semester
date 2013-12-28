package etude.foundation.domain.lifecycle.sync

import etude.foundation.domain.model.{Entity, Identity}
import etude.foundation.domain.lifecycle.ResultWithEntity
import scala.util.Try

trait SyncResultWithEntity[+R <: SyncEntityWriter[ID, E], ID <: Identity[_], E <: Entity[ID]]
  extends ResultWithEntity[R, ID, E, Try]

object SyncResultWithEntity {

}