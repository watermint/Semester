package etude.messaging.chatwork.domain.account

import etude.foundation.domain.model.Identity

case class OrganizationId(value: BigInt)
  extends Identity[BigInt]
