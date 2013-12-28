package etude.chatwork.domain.message

import etude.chatwork.domain.room.RoomId
import etude.foundation.domain.model.Identity

class MessageId(val roomId: RoomId,
                val messageId: BigInt)
  extends Identity[(RoomId, BigInt)] {

  val value: (RoomId, BigInt) = roomId -> messageId
}
