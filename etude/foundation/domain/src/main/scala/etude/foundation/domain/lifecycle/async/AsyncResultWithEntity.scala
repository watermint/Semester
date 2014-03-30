package etude.foundation.domain.lifecycle.async

import etude.foundation.domain.model.{Entity, Identity}
import etude.foundation.domain.lifecycle.ResultWithEntity
import scala.concurrent.Future

trait AsyncResultWithEntity[+R <: AsyncEntityWriter[ID, E], ID <: Identity[_], E <: Entity[ID]]
  extends ResultWithEntity[R, ID, E, Future]

object AsyncResultWithEntity {

  def apply[R <: AsyncEntityWriter[ID, T], ID <: Identity[_], T <: Entity[ID]](result: R, entity: T):
  AsyncResultWithEntity[R, ID, T] = AsyncResultWithEntityImpl(result, entity)

  def unapply[R <: AsyncEntityWriter[ID, T], ID <: Identity[_], T <: Entity[ID]](resultWithEntity: AsyncResultWithEntity[R, ID, T]): Option[(R, T)] =
    Some(resultWithEntity.result, resultWithEntity.entity)
}

private[async]
case class AsyncResultWithEntityImpl[+R <: AsyncEntityWriter[ID, T], ID <: Identity[_], T <: Entity[ID]](result: R, entity: T)
  extends AsyncResultWithEntity[R, ID, T]
