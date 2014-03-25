package etude.messaging.chatwork.infrastructure.api.v0

import scala.concurrent.{ExecutionContext, Future}
import etude.foundation.domain.lifecycle.async.AsyncEntityIOContext

trait V0AsyncEntityIOContext
  extends V0EntityIOContext[Future]
  with AsyncEntityIOContext

object V0AsyncEntityIOContext {
  def apply(organizationId: String, email: String, password: String)(implicit executor: ExecutionContext): V0AsyncEntityIOContext = {
    new V0AsyncEntityIOContextImpl(Some(organizationId), email, password)
  }

  def apply(email: String, password: String)(implicit executor: ExecutionContext): V0AsyncEntityIOContext = {
    new V0AsyncEntityIOContextImpl(None, email, password)
  }
}

private[v0]
class V0AsyncEntityIOContextImpl(val organizationId: Option[String],
                                 val username: String,
                                 val password: String)
                                (implicit val executor: ExecutionContext)
  extends V0AsyncEntityIOContext
