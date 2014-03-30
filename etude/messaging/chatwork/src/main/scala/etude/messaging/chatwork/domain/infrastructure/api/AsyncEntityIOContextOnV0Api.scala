package etude.messaging.chatwork.domain.infrastructure.api

import scala.concurrent.{ExecutionContext, Future}
import etude.foundation.domain.lifecycle.async.AsyncEntityIOContext

trait AsyncEntityIOContextOnV0Api
  extends EntityIOContextOnV0Api[Future]
  with AsyncEntityIOContext

object AsyncEntityIOContextOnV0Api {
  def apply(organizationId: String, email: String, password: String)(implicit executor: ExecutionContext): AsyncEntityIOContextOnV0Api = {
    new AsyncEntityIOContextOnV0ApiImpl(Some(organizationId), email, password)
  }

  def apply(email: String, password: String)(implicit executor: ExecutionContext): AsyncEntityIOContextOnV0Api = {
    new AsyncEntityIOContextOnV0ApiImpl(None, email, password)
  }
}

private[api]
class AsyncEntityIOContextOnV0ApiImpl(val organizationId: Option[String],
                                 val username: String,
                                 val password: String)
                                (implicit val executor: ExecutionContext)
  extends AsyncEntityIOContextOnV0Api
