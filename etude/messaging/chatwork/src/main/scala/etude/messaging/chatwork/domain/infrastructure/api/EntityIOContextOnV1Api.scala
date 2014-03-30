package etude.messaging.chatwork.domain.infrastructure.api

import scala.language.higherKinds
import etude.foundation.domain.lifecycle.EntityIOContext

trait EntityIOContextOnV1Api[M[+A]]
  extends EntityIOContext[M] {

  val token: String
}
