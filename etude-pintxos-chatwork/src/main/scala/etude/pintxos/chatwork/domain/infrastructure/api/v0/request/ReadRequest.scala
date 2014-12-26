package etude.pintxos.chatwork.domain.infrastructure.api.v0.request

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.infrastructure.api.v0.command.Read
import etude.pintxos.chatwork.domain.infrastructure.api.v0.response.ChatWorkResponse
import etude.pintxos.chatwork.domain.model.message.MessageId
import etude.pintxos.chatwork.domain.model.room.RoomId

import scala.concurrent.Future

case class ReadRequest(roomId: RoomId,
                       messageId: MessageId)
  extends ChatWorkRequest {
  def execute(implicit context: EntityIOContext[Future]): Future[ChatWorkResponse] = {
    Read.execute(this)
  }
}
