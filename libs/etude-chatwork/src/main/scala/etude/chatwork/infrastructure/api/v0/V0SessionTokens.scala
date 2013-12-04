package etude.chatwork.infrastructure.api.v0

import etude.http.Client

case class V0SessionTokens(client: Client,
                           accessToken: String,
                           myId: String)
