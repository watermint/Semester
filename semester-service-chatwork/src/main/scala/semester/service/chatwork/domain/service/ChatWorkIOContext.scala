package semester.service.chatwork.domain.service

import semester.foundation.http.Client
import semester.foundation.utilities.atomic.Reference
import semester.foundation.utilities.config.ThinConfig

import scala.concurrent.SyncVar
import scala.language.higherKinds

trait ChatWorkIOContext {

  val organizationId: Option[String]

  val username: String

  val password: String

  val client: Client = Client()

  val accessToken: SyncVar[String] = new SyncVar[String]

  val myId: SyncVar[String] = new SyncVar[String]

  val lastId: Reference[String] = Reference[String]()
}

object ChatWorkIOContext {
  def fromThinConfig(): ChatWorkIOContext = {
    ThinConfig.ofName("ChatWorkIOContext") match {
      case None => throw new IllegalStateException("thin config file not found")
      case Some(p) =>
        (p.get("org"), p.get("username"), p.get("password")) match {
          case (None, Some(username), Some(password)) =>
            apply(username, password)
          case (Some(""), Some(username), Some(password)) =>
            apply(username, password)
          case (Some(org), Some(username), Some(password)) =>
            apply(org, username, password)
          case _ =>
            throw new IllegalStateException("username and/or password not found in thin config")
        }
    }
  }

  def apply(organizationId: String, username: String, password: String): ChatWorkIOContext = {
    new ChatWorkIOContextImpl(Some(organizationId), username, password)
  }

  def apply(username: String, password: String): ChatWorkIOContext = {
    new ChatWorkIOContextImpl(None, username, password)
  }
}

private[service]
class ChatWorkIOContextImpl(val organizationId: Option[String],
                            val username: String,
                            val password: String)
  extends ChatWorkIOContext
