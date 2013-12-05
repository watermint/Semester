package etude.chatwork.model

import etude.commons.domain.Identity

class AccountId(id: BigInt) extends Identity[BigInt] {
  def value: BigInt = id
}
