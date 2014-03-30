package etude.messaging.chatwork.domain.infrastructure.api

import scala.language.higherKinds
import etude.foundation.http.SyncClient
import etude.foundation.domain.lifecycle.EntityIOContext

trait EntityIOContextOnV0Api[M[+A]]
  extends EntityIOContext[M] {

  val organizationId: Option[String]

  val username: String

  val password: String

  val client: SyncClient = SyncClient()

  var accessToken: Option[String] = None

  var myId: Option[String] = None
}
