package etude.chatwork.model

import etude.ddd.model.Identity

class MessageId(roomId: BigInt, messageId: BigInt) extends Identity[(BigInt, BigInt)] {
  def value: (BigInt, BigInt) = roomId -> messageId
}
