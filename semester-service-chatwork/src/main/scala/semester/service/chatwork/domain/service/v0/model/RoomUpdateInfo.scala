package semester.service.chatwork.domain.service.v0.model

import semester.service.chatwork.domain.model.message.MessageId
import semester.service.chatwork.domain.model.room.RoomId

case class RoomUpdateInfo(roomId: RoomId,
                          editedMessages: Seq[MessageId],
                          deletedMessages: Seq[MessageId])