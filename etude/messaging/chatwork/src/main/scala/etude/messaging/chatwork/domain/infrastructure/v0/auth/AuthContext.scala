package etude.messaging.chatwork.domain.infrastructure.v0.auth

import etude.foundation.http.Client
import java.net.URI

case class AuthContext(client: Client,
                       username: String,
                       password: String,
                       redirectedUri: Option[URI],
                       startPageUri: URI,
                       startPageContent: String)
