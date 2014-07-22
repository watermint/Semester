package etude.pintxos.chatwork.domain.model.account

import etude.manieres.domain.model.Entity

class Organization(val organizationId: OrganizationId,
                   val name: String)
  extends Entity[OrganizationId] {

  val identity: OrganizationId = organizationId
}
