package etude.messaging.chatwork.domain.model.room

import etude.domain.core.model.Identity

case class RoomId(value: BigInt)
  extends Identity[BigInt]
