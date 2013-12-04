package etude.chatwork.model

import etude.ddd.model.Identity

class AccountId(id: BigInt) extends Identity[BigInt] {
  def value: BigInt = id
}
