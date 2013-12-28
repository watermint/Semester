package etude.chatwork.infrastructure.api.v1

import scala.language.higherKinds
import etude.foundation.domain.lifecycle.EntityIOContext

trait V1EntityIOContext[M[+A]]
  extends EntityIOContext[M] {

  val token: String
}
