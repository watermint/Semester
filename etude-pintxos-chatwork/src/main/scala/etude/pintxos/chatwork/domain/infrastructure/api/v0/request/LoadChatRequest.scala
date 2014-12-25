package etude.pintxos.chatwork.domain.infrastructure.api.v0.request

import etude.pintxos.chatwork.domain.model.message.MessageId
import etude.pintxos.chatwork.domain.model.room.RoomId

case class LoadChatRequest(room: RoomId,
                           firstChatId: Option[MessageId] = None,
                           lastChatId: Option[MessageId] = None,
                           jumpToChatId: Option[MessageId] = None,
                           unreadNum: Boolean = false,
                           description: Boolean = false,
                           task: Boolean = false)
  extends ChatWorkRequest
