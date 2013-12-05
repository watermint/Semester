package etude.chatwork.model

import etude.commons.domain.Identity

class MessageId(roomId: BigInt, messageId: BigInt) extends Identity[(BigInt, BigInt)] {
  def value: (BigInt, BigInt) = roomId -> messageId
}
