package etude.pintxos.chatwork.domain.infrastructure.api.v0.request

import etude.pintxos.chatwork.domain.model.account.AccountId

case class GetAccountInfoRequest(accountIds: Seq[AccountId])
  extends ChatWorkRequest

