package etude.pintxos.chatwork.domain.service.v0.request

import etude.pintxos.chatwork.domain.model.message.MessageId
import etude.pintxos.chatwork.domain.model.room.RoomId
import etude.pintxos.chatwork.domain.service.v0.ChatWorkIOContext
import etude.pintxos.chatwork.domain.service.v0.command.Read
import etude.pintxos.chatwork.domain.service.v0.response.ChatWorkResponse

case class ReadRequest(roomId: RoomId,
                       messageId: MessageId)
  extends ChatWorkRequest {
  def execute(implicit context: ChatWorkIOContext): ChatWorkResponse = {
    Read.execute(this)
  }
}
