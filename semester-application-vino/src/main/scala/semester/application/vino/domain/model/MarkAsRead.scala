package semester.application.vino.domain.model

import semester.foundation.domain.model.Entity
import semester.service.chatwork.domain.model.room.RoomId

case class MarkAsRead(roomId: RoomId,
                      markAsRead: Boolean) extends Entity[RoomId] {
  val identity: RoomId = roomId
}
