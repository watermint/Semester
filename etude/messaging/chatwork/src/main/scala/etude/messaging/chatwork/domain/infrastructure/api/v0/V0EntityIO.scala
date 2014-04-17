package etude.messaging.chatwork.domain.infrastructure.api.v0

import scala.language.higherKinds
import etude.foundation.http.{Client, AsyncClient, SyncClient}
import etude.foundation.domain.lifecycle.{EntityIO, EntityIOContext}
import etude.messaging.chatwork.domain.infrastructure.api.EntityIOContextOnV0Api
import java.time.Instant
import java.util.concurrent.atomic.AtomicInteger

trait V0EntityIO[M[+A]]
  extends EntityIO {

  def contextAccessWaitInMillis = 500

  private def withV0Context[T](context: EntityIOContext[M])(f: EntityIOContextOnV0Api[M] => T): T = {
    context match {
      case v0: EntityIOContextOnV0Api[M] => f(v0)
      case _ => throw new IllegalArgumentException(s"$context must compatible with V0EntityIOContext")
    }
  }

  protected def getOrganizationId(context: EntityIOContext[M]): Option[String] = {
    withV0Context(context) {
      v0 => v0.organizationId
    }
  }

  protected def getUsername(context: EntityIOContext[M]): String = {
    withV0Context(context) {
      v0 => v0.username
    }
  }

  protected def getPassword(context: EntityIOContext[M]): String = {
    withV0Context(context) {
      v0 => v0.password
    }
  }

  protected def getClient(context: EntityIOContext[M]): SyncClient = {
    withV0Context(context) {
      v0 => v0.client
    }
  }

  protected def getAccessToken(context: EntityIOContext[M]): Option[String] = {
    withV0Context(context) {
      v0 => v0.accessToken.get(contextAccessWaitInMillis)
    }
  }

  protected def getMyId(context: EntityIOContext[M]): Option[String] = {
    withV0Context(context) {
      v0 => v0.myId.get(contextAccessWaitInMillis)
    }
  }

  protected def getLastId(context: EntityIOContext[M]): Option[String] = {
    withV0Context(context) {
      v0 => v0.lastId.get(contextAccessWaitInMillis)
    }
  }

  protected def setAccessToken(accessToken: String, context: EntityIOContext[M]): Unit = {
    withV0Context(context) {
      v0 => v0.accessToken.put(accessToken)
    }
  }

  protected def setMyId(myId: String, context: EntityIOContext[M]): Unit = {
    withV0Context(context) {
      v0 => v0.myId.put(myId)
    }
  }

  protected def setLastId(lastId: String, context: EntityIOContext[M]): Unit = {
    withV0Context(context) {
      v0 => v0.lastId.put(lastId)
    }
  }

  protected def beginLogin(context: EntityIOContext[M]): Unit = {
    withV0Context(context) {
      v0 => v0.loginMutex.acquire()
    }
  }

  protected def endLogin(context: EntityIOContext[M]): Unit = {
    withV0Context(context) {
      v0 => v0.loginMutex.acquire()
    }
  }

  protected def getLoginTime(context: EntityIOContext[M]): Option[Instant] = {
    withV0Context(context) {
      v0 => v0.loginTimestamp.get(contextAccessWaitInMillis)
    }
  }

  protected def setLoginTime(loginTime: Instant, context: EntityIOContext[M]): Unit = {
    withV0Context(context) {
      v0 => v0.loginTimestamp.put(loginTime)
    }
  }

  protected def clearToken(context: EntityIOContext[M]): Unit = {
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

  protected def hasToken(context: EntityIOContext[M]): Boolean = {
    withV0Context(context) {
      v0 =>
        v0.accessToken.isSet
    }
  }

  protected def getLoginFailure(context: EntityIOContext[M]): AtomicInteger = {
    withV0Context(context) {
      v0 =>
        v0.loginFailure
    }
  }
}
