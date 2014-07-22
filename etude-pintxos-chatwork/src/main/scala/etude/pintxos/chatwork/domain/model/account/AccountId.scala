package etude.pintxos.chatwork.domain.model.account

import etude.manieres.domain.model.Identity

case class AccountId(value: BigInt)
  extends Identity[BigInt]
