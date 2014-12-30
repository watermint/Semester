package etude.pintxos.chatwork.domain.infrastructure.api.v0

import java.time.Instant
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

import etude.epice.http.Client
import etude.manieres.domain.lifecycle.EntityIOContext
import etude.manieres.domain.lifecycle.async.AsyncEntityIO
import etude.pintxos.chatwork.domain.infrastructure.api.EntityIOContextOnV0Api

import scala.concurrent.Future
import scala.language.higherKinds

trait V0AsyncEntityIO
  extends AsyncEntityIO {

  val contextAccessWaitInMillis = 500
  val updateDelayInMills = 10000

  private def withV0Context[T](context: EntityIOContext[Future])(f: EntityIOContextOnV0Api[Future] => T): T = {
    context match {
      case v0: EntityIOContextOnV0Api[Future] => f(v0)
      case _ => throw new IllegalArgumentException(s"$context must compatible with V0EntityIOContext")
    }
  }

  protected def getOrganizationId(context: EntityIOContext[Future]): Option[String] = {
    withV0Context(context) {
      v0 => v0.organizationId
    }
  }

  protected def getUsername(context: EntityIOContext[Future]): String = {
    withV0Context(context) {
      v0 => v0.username
    }
  }

  protected def getPassword(context: EntityIOContext[Future]): String = {
    withV0Context(context) {
      v0 => v0.password
    }
  }

  protected def getClient(context: EntityIOContext[Future]): Client = {
    withV0Context(context) {
      v0 => v0.client
    }
  }

  protected def getAccessToken(context: EntityIOContext[Future]): Option[String] = {
    withV0Context(context) {
      v0 => v0.accessToken.get(contextAccessWaitInMillis)
    }
  }

  protected def getMyId(context: EntityIOContext[Future]): Option[String] = {
    withV0Context(context) {
      v0 => v0.myId.get(contextAccessWaitInMillis)
    }
  }

  protected def getLastId(context: EntityIOContext[Future]): Option[String] = {
    withV0Context(context) {
      v0 => v0.lastId.get() match {
        case null => None
        case l => Some(l)
      }
    }
  }

  protected def setAccessToken(accessToken: String, context: EntityIOContext[Future]): Unit = {
    withV0Context(context) {
      v0 => v0.accessToken.put(accessToken)
    }
  }

  protected def setMyId(myId: String, context: EntityIOContext[Future]): Unit = {
    withV0Context(context) {
      v0 => v0.myId.put(myId)
    }
  }

  protected def setLastId(lastId: String, context: EntityIOContext[Future]): Unit = {
    withV0Context(context) {
      v0 =>
        v0.lastId.set(lastId)
    }
  }

  protected def beginLogin(context: EntityIOContext[Future]): Unit = {
    withV0Context(context) {
      v0 => v0.loginMutex.lock()
    }
  }

  protected def endLogin(context: EntityIOContext[Future]): Unit = {
    withV0Context(context) {
      v0 => v0.loginMutex.unlock()
    }
  }

  protected def getLoginTime(context: EntityIOContext[Future]): Option[Instant] = {
    withV0Context(context) {
      v0 => v0.loginTimestamp.get(contextAccessWaitInMillis)
    }
  }

  protected def setLoginTime(loginTime: Instant, context: EntityIOContext[Future]): Unit = {
    withV0Context(context) {
      v0 => v0.loginTimestamp.put(loginTime)
    }
  }

  protected def clearToken(context: EntityIOContext[Future]): Unit = {
    withV0Context(context) {
      v0 =>
        try {
          v0.accessToken.take(contextAccessWaitInMillis)
        } catch {
          case _: java.util.NoSuchElementException => // ignore
          case _: Throwable => // ignore
        }
    }
  }

  protected def hasToken(context: EntityIOContext[Future]): Boolean = {
    withV0Context(context) {
      v0 =>
        v0.accessToken.isSet
    }
  }

  protected def getLoginFailure(context: EntityIOContext[Future]): AtomicInteger = {
    withV0Context(context) {
      v0 =>
        v0.loginFailure
    }
  }

  protected def resetContextSession(context: EntityIOContext[Future]): Unit = {
    clearToken(context)
    withV0Context(context) {
      v0 =>
        v0.client = Client()
    }
  }
}
