package etude.chatwork.infrastructure.api.v1

import etude.chatwork.infrastructure.api.TokenAuthentication

case class V1AuthToken(token: String) extends TokenAuthentication
