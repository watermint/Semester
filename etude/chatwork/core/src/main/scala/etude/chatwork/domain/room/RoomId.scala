package etude.chatwork.domain.room

import etude.foundation.domain.Identity

class RoomId(val id: BigInt) extends Identity[BigInt] {
  def value: BigInt = id
}
