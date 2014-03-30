package etude.messaging.chatwork.domain.infrastructure.api

import scala.concurrent.{ExecutionContext, Future}
import etude.foundation.domain.lifecycle.async.AsyncEntityIOContext
import etude.foundation.http.{Client, AsyncClient}
import etude.foundation.utility.ThinConfig

trait AsyncEntityIOContextOnV0Api
  extends EntityIOContextOnV0Api[Future]
  with AsyncEntityIOContext

object AsyncEntityIOContextOnV0Api {
  def fromThinConfig()(implicit executor: ExecutionContext): AsyncEntityIOContext = {
    ThinConfig.ofName("AsyncEntityIOContextOnV0Api") match {
      case None => throw new IllegalStateException("thin config file not found")
      case Some(p) =>
        (p.get("org"), p.get("username"), p.get("password")) match {
          case (Some(org), Some(username), Some(password)) =>
            apply(org, username, password)
          case (None, Some(username), Some(password)) =>
            apply(username, password)
          case ("", Some(username), Some(password)) =>
            apply(username, password)
          case _ =>
            throw new IllegalStateException("username and/or password not found in thin config")
        }
    }
  }
  
  def apply(organizationId: String, username: String, password: String)(implicit executor: ExecutionContext): AsyncEntityIOContext = {
    new AsyncEntityIOContextOnV0ApiImpl(Some(organizationId), username, password)
  }

  def apply(username: String, password: String)(implicit executor: ExecutionContext): AsyncEntityIOContextOnV0Api = {
    new AsyncEntityIOContextOnV0ApiImpl(None, username, password)
  }
}

private[api]
class AsyncEntityIOContextOnV0ApiImpl(val organizationId: Option[String],
                                      val username: String,
                                      val password: String)
                                     (implicit val executor: ExecutionContext)
  extends AsyncEntityIOContextOnV0Api
