package etude.chatwork.v1

import etude.ddd.model.Identity

class OrganizationId(id: BigInt) extends Identity[BigInt] {
  def value: BigInt = id
}
