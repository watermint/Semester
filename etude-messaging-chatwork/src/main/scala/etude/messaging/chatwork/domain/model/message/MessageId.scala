package etude.messaging.chatwork.domain.model.message

import etude.domain.core.model.Identity
import etude.messaging.chatwork.domain.model.room.RoomId

case class MessageId(roomId: RoomId,
                     messageId: BigInt)
  extends Identity[(RoomId, BigInt)] {

  val value: (RoomId, BigInt) = roomId -> messageId
}
