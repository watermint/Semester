package etude.messaging.chatwork.domain.model.account

import etude.domain.core.model.Identity

case class OrganizationId(value: BigInt)
  extends Identity[BigInt]
