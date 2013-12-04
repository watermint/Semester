package etude.chatwork.domain.message

import etude.ddd.model.Identity
import etude.chatwork.domain.room.RoomId

class MessageId(val roomId: RoomId,
                val messageId: BigInt)
  extends Identity[(RoomId, BigInt)] {

  def value: (RoomId, BigInt) = roomId -> messageId
}
