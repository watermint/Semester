package etude.pintxos.chatwork.domain.service.v0.response

import etude.pintxos.chatwork.domain.model.account.Account
import etude.pintxos.chatwork.domain.service.v0.request.GetAccountInfoRequest
import org.json4s.JValue

case class GetAccountInfoResponse(rawResponse: JValue,
                                  request: GetAccountInfoRequest,
                                  accounts: Seq[Account])
  extends ChatWorkResponse {
  type Request = GetAccountInfoRequest
}
