package etude.chatwork.domain.account

import etude.ddd.model.Identity

class OrganizationId(val id: BigInt) extends Identity[BigInt] {
  def value: BigInt = id
}
