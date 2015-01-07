package etude.pintxos.chatwork.domain.service.v0.request

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.service.v0.command.SendChat
import etude.pintxos.chatwork.domain.service.v0.response.{ChatWorkResponse, SendChatResponse}
import etude.pintxos.chatwork.domain.model.room.RoomId

import scala.concurrent.Future

case class SendChatRequest(text: String,
                           room: RoomId)
  extends ChatWorkRequest {
  def execute(implicit context: EntityIOContext[Future]): Future[ChatWorkResponse] = {
    SendChat.execute(this)
  }
}
