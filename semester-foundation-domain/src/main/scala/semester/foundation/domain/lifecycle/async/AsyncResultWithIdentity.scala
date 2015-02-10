package semester.foundation.domain.lifecycle.async

import semester.foundation.domain.lifecycle.ResultWithIdentity
import semester.foundation.domain.model.{Entity, Identity}

import scala.concurrent.Future

trait AsyncResultWithIdentity[+R <: AsyncEntityWriter[ID, E], ID <: Identity[_], E <: Entity[ID]]
  extends ResultWithIdentity[R, ID, E, Future]

object AsyncResultWithIdentity {

  def apply[R <: AsyncEntityWriter[ID, T], ID <: Identity[_], T <: Entity[ID]](result: R, identity: ID):
  AsyncResultWithIdentity[R, ID, T] = AsyncResultWithIdentityImpl(result, identity)

  def unapply[R <: AsyncEntityWriter[ID, T], ID <: Identity[_], T <: Entity[ID]](resultWithIdentity: AsyncResultWithIdentity[R, ID, T]): Option[(R, ID)] =
    Some(resultWithIdentity.result, resultWithIdentity.identity)
}

private[async]
case class AsyncResultWithIdentityImpl[+R <: AsyncEntityWriter[ID, T], ID <: Identity[_], T <: Entity[ID]](result: R, identity: ID)
  extends AsyncResultWithIdentity[R, ID, T]
