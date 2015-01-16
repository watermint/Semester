package etude.pintxos.chatwork.domain.service.v0.request

import etude.pintxos.chatwork.domain.model.room.RoomId
import etude.pintxos.chatwork.domain.service.v0.ChatWorkIOContext
import etude.pintxos.chatwork.domain.service.v0.command.SendChat
import etude.pintxos.chatwork.domain.service.v0.response.ChatWorkResponse

case class SendChatRequest(text: String,
                           room: RoomId)
  extends ChatWorkRequest {
  def execute(implicit context: ChatWorkIOContext): ChatWorkResponse = {
    SendChat.execute(this)
  }
}
