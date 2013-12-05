package etude.chatwork.domain.account

import etude.commons.domain.Identity

class OrganizationId(val id: BigInt) extends Identity[BigInt] {
  def value: BigInt = id
}
