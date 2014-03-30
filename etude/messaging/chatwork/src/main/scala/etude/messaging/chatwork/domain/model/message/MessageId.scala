package etude.messaging.chatwork.domain.model.message

import etude.messaging.chatwork.domain.model.room.RoomId
import etude.foundation.domain.model.Identity

case class MessageId(roomId: RoomId,
                     messageId: BigInt)
  extends Identity[(RoomId, BigInt)] {

  val value: (RoomId, BigInt) = roomId -> messageId
}
