package etude.pintxos.chatwork.domain.infrastructure.api.v0.response

import etude.pintxos.chatwork.domain.model.account.Account
import org.json4s.JValue

case class GetAccountInfoResponse(rawResponse: JValue,
                                  accounts: Seq[Account])
  extends ChatWorkResponse
