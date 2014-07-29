package etude.epice.sel.client

import etude.epice.sel.request.{Request, Verb}

trait Session {
  val context: Context

  def prepareRequest(verb: Verb): Request = ???
}
