package etude.messaging.chatwork.domain.model.account

import etude.foundation.domain.model.Identity

case class AccountId(value: BigInt)
  extends Identity[BigInt]
