package etude.chatwork.infrastructure.api

import etude.chatwork.infrastructure.api.v0.V0SessionContext
import etude.chatwork.infrastructure.api.v1.V1AuthToken

case class MixedAuthContext(organizationId: Option[String],
                            email: String,
                            password: String,
                            token: String)
  extends V0SessionContext
  with V1AuthToken
