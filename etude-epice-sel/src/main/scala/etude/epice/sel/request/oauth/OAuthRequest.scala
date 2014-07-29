package etude.epice.sel.request.oauth

import etude.epice.sel.client.Context
import etude.epice.sel.request.{Verb, Request}
import etude.epice.sel.response.Response

import scala.util.Try

class OAuthRequest extends Request {
  val verb: Verb = ???

  def execute(): Try[Response] = ???

  val context: Context = ???
}
