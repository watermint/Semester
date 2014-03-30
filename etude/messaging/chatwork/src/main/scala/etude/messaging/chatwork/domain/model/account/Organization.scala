package etude.messaging.chatwork.domain.model.account

import etude.foundation.domain.model.Entity

class Organization(val organizationId: OrganizationId,
                   val name: String)
  extends Entity[OrganizationId] {

  val identity: OrganizationId = organizationId
}
