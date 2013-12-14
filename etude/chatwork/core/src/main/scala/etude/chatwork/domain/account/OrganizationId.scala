package etude.chatwork.domain.account

import etude.foundation.domain.Identity

class OrganizationId(val id: BigInt) extends Identity[BigInt] {
  def value: BigInt = id
}
