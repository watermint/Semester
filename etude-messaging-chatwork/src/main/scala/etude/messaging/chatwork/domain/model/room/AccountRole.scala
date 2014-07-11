package etude.messaging.chatwork.domain.model.room

import etude.messaging.chatwork.domain.model.account.AccountId

case class AccountRole(accountId: AccountId, role: AccountRoleType)
