package etude.messaging.chatwork.domain.infrastructure.api

import scala.concurrent.{ExecutionContext, Future}
import etude.domain.core.lifecycle.async.AsyncEntityIOContext
import etude.foundation.utility.ThinConfig

trait AsyncEntityIOContextOnV1Api
  extends EntityIOContextOnV1Api[Future]
  with AsyncEntityIOContext

object AsyncEntityIOContextOnV1Api {
  def fromThinConfig()(implicit executor: ExecutionContext): AsyncEntityIOContext = {
    ThinConfig.ofName("AsyncEntityIOContextOnV1Api") match {
      case None => throw new IllegalStateException("thin config file not found")
      case Some(p) =>
        p.get("token") match {
          case Some(token) =>
            apply(token)
          case _ =>
            throw new IllegalStateException("username and/or password not found in thin config")
        }
    }
  }

  def apply(token: String)(implicit executor: ExecutionContext): AsyncEntityIOContext =
    AsyncEntityIOContextOnV1ApiImpl(token)
}

private[api]
case class AsyncEntityIOContextOnV1ApiImpl(token: String)(implicit val executor: ExecutionContext)
  extends AsyncEntityIOContextOnV1Api
