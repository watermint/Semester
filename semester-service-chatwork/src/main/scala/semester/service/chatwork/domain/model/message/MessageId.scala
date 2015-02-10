package semester.service.chatwork.domain.model.message

import semester.foundation.domain.model.Identity
import semester.service.chatwork.domain.model.room.RoomId

case class MessageId(roomId: RoomId,
                     messageId: BigInt)
  extends Identity[(RoomId, BigInt)] {

  val value: (RoomId, BigInt) = roomId -> messageId
}
