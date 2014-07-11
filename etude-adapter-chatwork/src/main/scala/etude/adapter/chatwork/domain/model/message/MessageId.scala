package etude.adapter.chatwork.domain.model.message

import etude.domain.core.model.Identity
import etude.adapter.chatwork.domain.model.room.RoomId

case class MessageId(roomId: RoomId,
                     messageId: BigInt)
  extends Identity[(RoomId, BigInt)] {

  val value: (RoomId, BigInt) = roomId -> messageId
}
