package etude.pintxos.chatwork.domain.infrastructure.api.v1

import etude.manieres.domain.lifecycle.{EntityIO, EntityIOContext}
import etude.pintxos.chatwork.domain.infrastructure.api.EntityIOContextOnV1Api

trait V1EntityIO[M[+A]]
  extends EntityIO {

  protected def getApiToken(context: EntityIOContext[M]): String = {
    context match {
      case v1: EntityIOContextOnV1Api[M] => v1.token
      case _ => throw new IllegalArgumentException(s"$context is not compatible with V1EntityIOContext")
    }
  }
}
