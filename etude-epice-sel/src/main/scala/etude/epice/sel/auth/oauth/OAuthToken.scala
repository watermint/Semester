package etude.epice.sel.auth.oauth

trait OAuthToken {
  val token: String
  val secret: String
  val raw: String
}

case class OAuthAccessToken(token: String,
                            secret: String,
                            raw: String) extends OAuthToken

case class OAuthRequestToken(token: String,
                             secret: String,
                             raw: String) extends OAuthToken
