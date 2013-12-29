package etude.chatwork.domain.account

import etude.foundation.domain.model.Identity

case class AccountId(value: BigInt)
  extends Identity[BigInt]
