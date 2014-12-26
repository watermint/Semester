package etude.pintxos.chatwork.domain.infrastructure.api.v0.request

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.infrastructure.api.v0.command.GetAccountInfo
import etude.pintxos.chatwork.domain.infrastructure.api.v0.response.{ChatWorkResponse, GetAccountInfoResponse}
import etude.pintxos.chatwork.domain.model.account.AccountId

import scala.concurrent.Future

case class GetAccountInfoRequest(accountIds: Seq[AccountId])
  extends ChatWorkRequest {
  def execute(implicit context: EntityIOContext[Future]): Future[ChatWorkResponse] = {
    GetAccountInfo.execute(this)
  }
}

