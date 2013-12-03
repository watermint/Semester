package etude.chatwork.v1

import etude.ddd.model.Identity

class AccountId(id: BigInt) extends Identity[BigInt] {
  def value: BigInt = id
}
