package etude.chatwork.infrastructure.api.v0

import etude.http.Client
import java.time.Instant

case class V0SessionContext(client: Client,
                          accessToken: String,
                          myId: String,
                          loginTime: Instant)
