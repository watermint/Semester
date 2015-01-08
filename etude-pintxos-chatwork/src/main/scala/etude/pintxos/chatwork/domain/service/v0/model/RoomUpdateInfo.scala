package etude.pintxos.chatwork.domain.service.v0.model

import etude.pintxos.chatwork.domain.model.message.MessageId
import etude.pintxos.chatwork.domain.model.room.RoomId

case class RoomUpdateInfo(roomId: RoomId,
                          editedMessages: Seq[MessageId],
                          deletedMessages: Seq[MessageId])