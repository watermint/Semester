package etude.chatwork.domain.account

import etude.chatwork.domain.JSONSerializable
import etude.commons.domain.Entity

class Organization(val organizationId: OrganizationId,
                   val name: String)
  extends Entity[OrganizationId]
  with JSONSerializable {

  val identity: OrganizationId = organizationId
}
