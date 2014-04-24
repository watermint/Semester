package etude.domain.core.lifecycle.sync

import etude.domain.core.model.{Entity, Identity}
import etude.domain.core.lifecycle.ResultWithEntity
import scala.util.Try

trait SyncResultWithEntity[+R <: SyncEntityWriter[ID, E], ID <: Identity[_], E <: Entity[ID]]
  extends ResultWithEntity[R, ID, E, Try]

object SyncResultWithEntity {

}