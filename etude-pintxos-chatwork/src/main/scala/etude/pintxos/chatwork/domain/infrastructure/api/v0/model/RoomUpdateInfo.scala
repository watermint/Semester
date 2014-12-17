package etude.pintxos.chatwork.domain.infrastructure.api.v0.model

import etude.manieres.domain.event.{IdentityEventType, IdentityEvent}
import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.lifecycle.room.AsyncRoomRepository
import etude.pintxos.chatwork.domain.model.message.MessageId
import etude.pintxos.chatwork.domain.model.room.RoomId

import scala.concurrent.{Future, Await}
import scala.concurrent.duration._

case class RoomUpdateInfo(roomId: RoomId,
                      editedMessages: Seq[MessageId],
                      deletedMessages: Seq[MessageId]) {

  val fetchMessageTimeoutMillis = 10000

  def latestMessage(implicit context: EntityIOContext[Future]): MessageId = Await.result(
    AsyncRoomRepository.ofContext(context).latestMessage(roomId)(context),
    Duration(fetchMessageTimeoutMillis, MILLISECONDS)
  )

  def asIdentityEvent(implicit context: EntityIOContext[Future]): Seq[IdentityEvent[MessageId]] = {
    editedMessages.map {
      m =>
        new IdentityEvent[MessageId](m, IdentityEventType.EntityStored)
    } ++ deletedMessages.map {
      m =>
        new IdentityEvent[MessageId](m, IdentityEventType.EntityDeleted)
    } :+
      new IdentityEvent[MessageId](latestMessage, IdentityEventType.EntityStored)
  }
}
