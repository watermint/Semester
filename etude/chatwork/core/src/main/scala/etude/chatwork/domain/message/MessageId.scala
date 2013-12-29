package etude.chatwork.domain.message

import etude.chatwork.domain.room.RoomId
import etude.foundation.domain.model.Identity

case class MessageId(roomId: RoomId,
                     messageId: BigInt)
  extends Identity[(RoomId, BigInt)] {

  val value: (RoomId, BigInt) = roomId -> messageId
}
