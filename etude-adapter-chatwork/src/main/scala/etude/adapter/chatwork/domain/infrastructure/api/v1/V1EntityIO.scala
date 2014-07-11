package etude.adapter.chatwork.domain.infrastructure.api.v1

import etude.domain.core.lifecycle.{EntityIO, EntityIOContext}
import etude.adapter.chatwork.domain.infrastructure.api.EntityIOContextOnV1Api

trait V1EntityIO[M[+A]]
  extends EntityIO {

  protected def getApiToken(context: EntityIOContext[M]): String = {
    context match {
      case v1: EntityIOContextOnV1Api[M] => v1.token
      case _ => throw new IllegalArgumentException(s"$context is not compatible with V1EntityIOContext")
    }
  }
}
