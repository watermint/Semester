package etude.chatwork.domain.account

import etude.ddd.model.Entity
import etude.chatwork.domain.JSONSerializable

class Organization(val organizationId: OrganizationId,
                   val name: String)
  extends Entity[OrganizationId]
  with JSONSerializable {

  val identity: OrganizationId = organizationId
}
