package semester.service.chatwork.domain.service.request

import semester.service.chatwork.domain.model.account.AccountId
import semester.service.chatwork.domain.service.ChatWorkIOContext
import semester.service.chatwork.domain.service.command.GetAccountInfo
import semester.service.chatwork.domain.service.response.ChatWorkResponse

case class GetAccountInfoRequest(accountIds: Seq[AccountId])
  extends ChatWorkRequest {
  def execute(implicit context: ChatWorkIOContext): ChatWorkResponse = {
    GetAccountInfo.execute(this)
  }
}

