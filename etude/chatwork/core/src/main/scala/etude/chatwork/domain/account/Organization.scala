package etude.chatwork.domain.account

import etude.foundation.domain.Entity

class Organization(val organizationId: OrganizationId,
                   val name: String)
  extends Entity[OrganizationId] {

  val identity: OrganizationId = organizationId
}
