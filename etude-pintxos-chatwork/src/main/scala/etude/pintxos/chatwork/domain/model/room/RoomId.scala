package etude.pintxos.chatwork.domain.model.room

import etude.manieres.domain.model.Identity

case class RoomId(value: BigInt)
  extends Identity[BigInt]
