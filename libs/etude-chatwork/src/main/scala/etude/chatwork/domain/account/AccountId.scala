package etude.chatwork.domain.account

import etude.commons.domain.Identity

class AccountId(val id: BigInt) extends Identity[BigInt] {
  def value: BigInt = id
}
