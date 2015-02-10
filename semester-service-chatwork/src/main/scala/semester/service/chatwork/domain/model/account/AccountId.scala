package semester.service.chatwork.domain.model.account

import semester.foundation.domain.model.Identity

case class AccountId(value: BigInt)
  extends Identity[BigInt]
