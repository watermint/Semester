package etude.chatwork.infrastructure.api.v1

import scala.concurrent.{ExecutionContext, Future}
import etude.foundation.domain.lifecycle.async.AsyncEntityIOContext

trait V1AsyncEntityIOContext
  extends V1EntityIOContext[Future]
  with AsyncEntityIOContext

object V1AsyncEntityIOContext {
  def apply(token: String)(implicit executor: ExecutionContext): V1AsyncEntityIOContext =
    V1AsyncEntityIOContextImpl(token)
}

private[v1]
case class V1AsyncEntityIOContextImpl(token: String)(implicit val executor: ExecutionContext)
  extends V1AsyncEntityIOContext
