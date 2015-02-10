package semester.service.chatwork.domain.service.v0.request

import semester.service.chatwork.domain.model.account.AccountId
import semester.service.chatwork.domain.service.v0.ChatWorkIOContext
import semester.service.chatwork.domain.service.v0.command.GetAccountInfo
import semester.service.chatwork.domain.service.v0.response.ChatWorkResponse

case class GetAccountInfoRequest(accountIds: Seq[AccountId])
  extends ChatWorkRequest {
  def execute(implicit context: ChatWorkIOContext): ChatWorkResponse = {
    GetAccountInfo.execute(this)
  }
}

