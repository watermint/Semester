package etude.epice.sel.client

import etude.epice.sel.signature.Signer

class OAuthSession extends Session {
  val context: Context = ???

  def signer(): Signer = ???
}
