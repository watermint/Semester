package etude.messaging.chatwork.domain.infrastructure.api

import scala.concurrent.{ExecutionContext, Future}
import etude.foundation.domain.lifecycle.async.AsyncEntityIOContext

trait AsyncEntityIOContextOnV1Api
  extends EntityIOContextOnV1Api[Future]
  with AsyncEntityIOContext

object AsyncEntityIOContextOnV1Api {
  def apply(token: String)(implicit executor: ExecutionContext): AsyncEntityIOContextOnV1Api =
    AsyncEntityIOContextOnV1ApiImpl(token)
}

private[api]
case class AsyncEntityIOContextOnV1ApiImpl(token: String)(implicit val executor: ExecutionContext)
  extends AsyncEntityIOContextOnV1Api
