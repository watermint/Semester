package etude.pintxos.chatwork.domain.model.room

import etude.pintxos.chatwork.domain.model.account.AccountId

case class AccountRole(accountId: AccountId, role: AccountRoleType)
