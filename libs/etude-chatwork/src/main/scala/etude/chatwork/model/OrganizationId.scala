package etude.chatwork.model

import etude.commons.domain.Identity

class OrganizationId(id: BigInt) extends Identity[BigInt] {
  def value: BigInt = id
}
