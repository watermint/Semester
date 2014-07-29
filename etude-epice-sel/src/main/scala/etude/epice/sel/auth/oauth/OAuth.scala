package etude.epice.sel.auth.oauth

import java.net.URI

import etude.epice.sel.auth.Auth
import etude.epice.sel.request.Request

import scala.util.Try

trait OAuth extends Auth {
  def version(): String

  def authorizationUrl(requestToken: OAuthRequestToken): Try[URI]

  def requestToken(): Try[OAuthRequestToken]

  def accessToken(verifier: OAuthVerifier)
                 (requestToken: OAuthRequestToken): Try[OAuthAccessToken]

  def signature(request: Request)
               (accessToken: OAuthAccessToken): Try[Request]
}
