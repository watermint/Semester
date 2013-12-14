package etude.chatwork.domain.message

import etude.chatwork.domain.room.RoomId
import etude.foundation.domain.Identity

class MessageId(val roomId: RoomId,
                val messageId: BigInt)
  extends Identity[(RoomId, BigInt)] {

  def value: (RoomId, BigInt) = roomId -> messageId
}
