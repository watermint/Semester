package etude.chatwork.model

import etude.ddd.model.Identity

class RoomId(id: BigInt) extends Identity[BigInt] {
  def value: BigInt = id
}
