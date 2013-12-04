package etude.chatwork.domain.room

import etude.ddd.model.Identity

class RoomId(val id: BigInt) extends Identity[BigInt] {
  def value: BigInt = id
}
