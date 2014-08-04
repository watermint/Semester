package etude.epice.sel.client

import etude.epice.sel.policy.Policy
import etude.epice.sel.request.{Request, Verb}
import etude.epice.sel.response.Response

trait Session[RQ <: Request[RS], RS <: Response] {
  val context: Context

  def prepareRequest(verb: Verb): RQ = ???

  def withPolicy[S <: Session[RQ, RS]](policy: Policy): S = ???
}
