package etude.messaging.chatwork.domain.infrastructure.api.v0.auth

import etude.foundation.http.SyncClient
import java.net.URI

case class AuthContext(client: SyncClient,
                       username: String,
                       password: String,
                       redirectedUri: Option[URI],
                       startPageUri: URI,
                       startPageContent: String)
