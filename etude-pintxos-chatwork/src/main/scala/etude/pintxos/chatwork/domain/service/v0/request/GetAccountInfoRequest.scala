package etude.pintxos.chatwork.domain.service.v0.request

import etude.pintxos.chatwork.domain.model.account.AccountId
import etude.pintxos.chatwork.domain.service.v0.ChatWorkIOContext
import etude.pintxos.chatwork.domain.service.v0.command.GetAccountInfo
import etude.pintxos.chatwork.domain.service.v0.response.ChatWorkResponse

case class GetAccountInfoRequest(accountIds: Seq[AccountId])
  extends ChatWorkRequest {
  def execute(implicit context: ChatWorkIOContext): ChatWorkResponse = {
    GetAccountInfo.execute(this)
  }
}

