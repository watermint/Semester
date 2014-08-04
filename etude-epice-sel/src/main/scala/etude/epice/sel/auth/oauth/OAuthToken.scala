package etude.epice.sel.auth.oauth

import etude.epice.sel.auth.AuthResult

trait OAuthToken {
  val token: String
  val secret: String
  val raw: String
}

case class OAuthAccessToken(token: String,
                            secret: String,
                            raw: String) extends OAuthToken with AuthResult

case class OAuthRequestToken(token: String,
                             secret: String,
                             raw: String) extends OAuthToken
