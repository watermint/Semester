package etude.chatwork.v1

import etude.ddd.model.Identity

class MessageId(id: BigInt) extends Identity[BigInt] {
  def value: BigInt = id
}
