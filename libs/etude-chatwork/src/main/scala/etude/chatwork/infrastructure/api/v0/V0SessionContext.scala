package etude.chatwork.infrastructure.api.v0

import etude.chatwork.infrastructure.api.AuthContext

trait V0SessionContext extends AuthContext {

  val organizationId: Option[String]

  val user: String

  val password: String

  var tokens: Option[V0SessionTokens] = None
}
