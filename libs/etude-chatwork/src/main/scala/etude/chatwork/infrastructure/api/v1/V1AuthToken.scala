package etude.chatwork.infrastructure.api.v1

import etude.chatwork.infrastructure.api.AuthContext

trait V1AuthToken extends AuthContext {
  val token: String
}
