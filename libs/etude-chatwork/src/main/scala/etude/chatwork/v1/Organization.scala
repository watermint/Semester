package etude.chatwork.v1

import etude.ddd.model.Entity

class Organization(val organizationId: OrganizationId,
                   val name: String)
  extends Entity[OrganizationId] {

  val identity: OrganizationId = organizationId
}
