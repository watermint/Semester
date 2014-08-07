package etude.epice.sel.client

import etude.epice.sel.request.Request
import etude.epice.sel.response.Response

trait Client {
  val context: Context
}

object Client {
  def apply(): Client = ???

  def ofContext(context: Context): Client = ???

  def ifAuthorized(request: Request): Response = ???

  def execute(request: Request): Response = ???
}
