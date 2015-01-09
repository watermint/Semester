package etude.pintxos.chatwork.domain.service.v0

import java.util.concurrent.atomic.AtomicReference

import etude.epice.http.Client
import etude.epice.utility.config.ThinConfig

import scala.concurrent.SyncVar
import scala.language.higherKinds

trait ChatWorkIOContext {

  val organizationId: Option[String]

  val username: String

  val password: String

  val client: Client = Client()

  val accessToken: SyncVar[String] = new SyncVar[String]

  val myId: SyncVar[String] = new SyncVar[String]

  val lastId: AtomicReference[String] = new AtomicReference[String]
}

object ChatWorkIOContext {
  def fromThinConfig(): ChatWorkIOContext = {
    ThinConfig.ofName("AsyncEntityIOContextOnV0Api") match {
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

private[v0]
class ChatWorkIOContextImpl(val organizationId: Option[String],
                            val username: String,
                            val password: String)
  extends ChatWorkIOContext
