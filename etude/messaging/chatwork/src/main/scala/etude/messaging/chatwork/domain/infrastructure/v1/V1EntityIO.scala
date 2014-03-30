package etude.messaging.chatwork.domain.infrastructure.v1

import etude.foundation.domain.lifecycle.{EntityIO, EntityIOContext}

trait V1EntityIO[M[+A]]
  extends EntityIO {

  protected def getApiToken(context: EntityIOContext[M]): String = {
    context match {
      case v1: V1EntityIOContext[M] => v1.token
      case _ => throw new IllegalArgumentException(s"$context is not compatible with V1EntityIOContext")
    }
  }
}
