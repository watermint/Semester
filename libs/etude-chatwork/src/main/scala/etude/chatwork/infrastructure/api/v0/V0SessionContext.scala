package etude.chatwork.infrastructure.api.v0

import etude.chatwork.infrastructure.api.PasswordAuthentication

case class V0SessionContext(organizationId: Option[String],
                            user: String,
                            password: String)
  extends PasswordAuthentication {

  var tokens: Option[V0SessionTokens] = None
}

