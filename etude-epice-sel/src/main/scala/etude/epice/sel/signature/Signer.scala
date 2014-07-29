package etude.epice.sel.signature

import etude.epice.sel.request.Payload

trait Signer {
  def signature(payload: Payload): Payload
}
