package etude.chatwork.domain.room

import etude.commons.domain.Identity

class RoomId(val id: BigInt) extends Identity[BigInt] {
  def value: BigInt = id
}
