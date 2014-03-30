package etude.messaging.chatwork.domain.infrastructure.v0

import scala.language.higherKinds
import etude.foundation.http.Client
import etude.foundation.domain.lifecycle.EntityIOContext

trait V0EntityIOContext[M[+A]]
  extends EntityIOContext[M] {

  val organizationId: Option[String]

  val username: String

  val password: String

  val client: Client = Client()

  var accessToken: Option[String] = None

  var myId: Option[String] = None
}
