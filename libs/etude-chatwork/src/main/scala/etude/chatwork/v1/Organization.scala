package etude.chatwork.v1

case class Organization(organizationId: OrganizationId,
                        name: String) extends Entity[OrganizationId]
