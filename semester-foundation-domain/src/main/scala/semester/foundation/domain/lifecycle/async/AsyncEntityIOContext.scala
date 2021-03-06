package semester.foundation.domain.lifecycle.async

import semester.foundation.domain.lifecycle.EntityIOContext

import scala.concurrent.{ExecutionContext, Future}

trait AsyncEntityIOContext
  extends EntityIOContext[Future] {

  val executor: ExecutionContext
}

object AsyncEntityIOContext {
  def apply()(implicit executor: ExecutionContext): AsyncEntityIOContext =
    new AsyncEntityIOContextImpl()

  def unapply(context: AsyncEntityIOContext): Option[(ExecutionContext)] =
    Some(context.executor)
}

private[async] class AsyncEntityIOContextImpl(implicit val executor: ExecutionContext)
  extends AsyncEntityIOContext
