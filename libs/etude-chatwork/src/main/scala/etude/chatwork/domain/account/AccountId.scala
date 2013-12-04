package etude.chatwork.domain.account

import etude.ddd.model.Identity

class AccountId(val id: BigInt) extends Identity[BigInt] {
  def value: BigInt = id
}
