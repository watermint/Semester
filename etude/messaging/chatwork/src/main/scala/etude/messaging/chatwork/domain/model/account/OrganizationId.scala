package etude.messaging.chatwork.domain.model.account

import etude.foundation.domain.model.Identity

case class OrganizationId(value: BigInt)
  extends Identity[BigInt]
