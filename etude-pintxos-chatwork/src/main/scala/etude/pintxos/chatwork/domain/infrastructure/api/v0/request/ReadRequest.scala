package etude.pintxos.chatwork.domain.infrastructure.api.v0.request

import etude.pintxos.chatwork.domain.model.message.MessageId
import etude.pintxos.chatwork.domain.model.room.RoomId

case class ReadRequest(roomId: RoomId,
                       messageId: MessageId)
  extends ChatWorkRequest
