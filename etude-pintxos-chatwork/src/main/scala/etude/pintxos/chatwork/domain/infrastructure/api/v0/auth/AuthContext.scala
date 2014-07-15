package etude.pintxos.chatwork.domain.infrastructure.api.v0.auth

import java.net.URI

import etude.foundation.http.SyncClient

case class AuthContext(client: SyncClient,
                       username: String,
                       password: String,
                       redirectedUri: Option[URI],
                       startPageUri: URI,
                       startPageContent: String)
