package semester.service.chatwork.domain.service.auth

import java.net.URI

import semester.foundation.http.Client

case class AuthContext(client: Client,
                       username: String,
                       password: String,
                       redirectedUri: Option[URI],
                       startPageUri: URI,
                       startPageContent: String)
