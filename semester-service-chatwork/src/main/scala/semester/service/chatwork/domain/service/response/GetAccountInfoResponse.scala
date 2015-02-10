package semester.service.chatwork.domain.service.response

import semester.service.chatwork.domain.model.account.Account
import semester.service.chatwork.domain.service.request.GetAccountInfoRequest
import org.json4s.JValue

case class GetAccountInfoResponse(rawResponse: JValue,
                                  request: GetAccountInfoRequest,
                                  accounts: Seq[Account])
  extends ChatWorkResponse {
  type Request = GetAccountInfoRequest
}
