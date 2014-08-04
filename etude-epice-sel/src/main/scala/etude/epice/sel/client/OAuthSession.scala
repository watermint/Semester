package etude.epice.sel.client

import etude.epice.sel.request.Request
import etude.epice.sel.response.Response
import etude.epice.sel.signature.Signer

class OAuthSession[RQ <: Request[RS], RS <: Response] extends AuthSession[RQ, RS] {
  val context: Context = ???

  def signer(): Signer = ???
}
