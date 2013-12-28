package etude.foundation.domain.lifecycle.async

import etude.foundation.domain.model.{Entity, Identity}
import etude.foundation.domain.lifecycle.ResultWithEntity
import scala.concurrent.Future

trait AsyncResultWithEntity[+R <: AsyncEntityWriter[ID, E], ID <: Identity[_], E <: Entity[ID]]
  extends ResultWithEntity[R, ID, E, Future]
