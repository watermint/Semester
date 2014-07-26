package etude.pintxos.chatwork.domain.infrastructure.api.v0.auth

import java.net.URI

import etude.epice.http.Client

case class AuthContext(client: Client,
                       username: String,
                       password: String,
                       redirectedUri: Option[URI],
                       startPageUri: URI,
                       startPageContent: String)
