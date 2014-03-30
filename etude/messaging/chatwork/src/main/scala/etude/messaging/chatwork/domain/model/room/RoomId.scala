package etude.messaging.chatwork.domain.model.room

import etude.foundation.domain.model.Identity

case class RoomId(value: BigInt)
  extends Identity[BigInt]
