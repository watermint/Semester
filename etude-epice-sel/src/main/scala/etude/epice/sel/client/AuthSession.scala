package etude.epice.sel.client

import etude.epice.sel.request.Request
import etude.epice.sel.response.Response

trait AuthSession[RQ <: Request[RS], RS <: Response] extends Session[RQ, RS] {
}
