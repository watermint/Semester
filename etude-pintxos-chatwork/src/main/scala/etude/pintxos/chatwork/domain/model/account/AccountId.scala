package etude.pintxos.chatwork.domain.model.account

import etude.domain.core.model.Identity

case class AccountId(value: BigInt)
  extends Identity[BigInt]
