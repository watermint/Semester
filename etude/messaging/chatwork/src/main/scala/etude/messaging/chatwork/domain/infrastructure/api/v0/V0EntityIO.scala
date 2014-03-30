package etude.messaging.chatwork.domain.infrastructure.api.v0

import scala.language.higherKinds
import etude.foundation.http.{Client, AsyncClient, SyncClient}
import etude.foundation.domain.lifecycle.{EntityIO, EntityIOContext}
import etude.messaging.chatwork.domain.infrastructure.api.EntityIOContextOnV0Api

trait V0EntityIO[M[+A]]
  extends EntityIO {

  protected def getOrganizationId(context: EntityIOContext[M]): Option[String] = {
    context match {
      case v0: EntityIOContextOnV0Api[M] => v0.organizationId
      case _ => throw new IllegalArgumentException(s"$context must compatible with V0EntityIOContext")
    }
  }

  protected def getUsername(context: EntityIOContext[M]): String = {
    context match {
      case v0: EntityIOContextOnV0Api[M] => v0.username
      case _ => throw new IllegalArgumentException(s"$context must compatible with V0EntityIOContext")
    }
  }

  protected def getPassword(context: EntityIOContext[M]): String = {
    context match {
      case v0: EntityIOContextOnV0Api[M] => v0.password
      case _ => throw new IllegalArgumentException(s"$context must compatible with V0EntityIOContext")
    }
  }

  protected def getClient(context: EntityIOContext[M]): SyncClient = {
    context match {
      case v0: EntityIOContextOnV0Api[M] => v0.client
      case _ => throw new IllegalArgumentException(s"$context must compatible with V0EntityIOContext")
    }
  }

  protected def getAccessToken(context: EntityIOContext[M]): Option[String] = {
    context match {
      case v0: EntityIOContextOnV0Api[M] => v0.accessToken
      case _ => throw new IllegalArgumentException(s"$context must compatible with V0EntityIOContext")
    }
  }

  protected def getMyId(context: EntityIOContext[M]): Option[String] = {
    context match {
      case v0: EntityIOContextOnV0Api[M] => v0.myId
      case _ => throw new IllegalArgumentException(s"$context must compatible with V0EntityIOContext")
    }
  }

  protected def setAccessToken(accessToken: Option[String], context: EntityIOContext[M]): Unit = {
    context match {
      case v0: EntityIOContextOnV0Api[M] => v0.accessToken = accessToken
      case _ => throw new IllegalArgumentException(s"$context must compatible with V0EntityIOContext")
    }
  }

  protected def setMyId(myId: Option[String], context: EntityIOContext[M]): Unit = {
    context match {
      case v0: EntityIOContextOnV0Api[M] => v0.myId = myId
      case _ => throw new IllegalArgumentException(s"$context must compatible with V0EntityIOContext")
    }
  }

  protected def clearToken(context: EntityIOContext[M]): Unit = {
    context match {
      case v0: EntityIOContextOnV0Api[M] =>
        v0.myId = None
        v0.accessToken = None
      case _ => throw new IllegalArgumentException(s"$context must compatible with V0EntityIOContext")
    }
  }

  protected def hasToken(context: EntityIOContext[M]): Boolean = {
    getAccessToken(context).isDefined
  }
}
