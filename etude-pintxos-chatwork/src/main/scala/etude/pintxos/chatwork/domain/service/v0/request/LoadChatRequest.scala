package etude.pintxos.chatwork.domain.service.v0.request

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.service.v0.command.LoadChat
import etude.pintxos.chatwork.domain.service.v0.response.{ChatWorkResponse, LoadChatResponse}
import etude.pintxos.chatwork.domain.model.message.MessageId
import etude.pintxos.chatwork.domain.model.room.RoomId

import scala.concurrent.Future

case class LoadChatRequest(room: RoomId,
                           firstChatId: Option[MessageId] = None,
                           lastChatId: Option[MessageId] = None,
                           jumpToChatId: Option[MessageId] = None,
                           unreadNum: Boolean = false,
                           description: Boolean = false,
                           task: Boolean = false)
  extends ChatWorkRequest {
  def execute(implicit context: EntityIOContext[Future]): Future[ChatWorkResponse] = {
    LoadChat.execute(this)
  }
}
