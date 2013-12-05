package etude.chatwork.model

import etude.commons.domain.Entity

class Organization(val organizationId: OrganizationId,
                   val name: String)
  extends Entity[OrganizationId] {

  val identity: OrganizationId = organizationId
}
