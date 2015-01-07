package etude.pintxos.chatwork.domain.lifecycle.message

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.service.v0.command.{LoadOldChat, Read, SendChat}
import etude.pintxos.chatwork.domain.service.v0.request.{SendChatRequest, ReadRequest, LoadOldChatRequest}
import etude.pintxos.chatwork.domain.model.message._
import etude.pintxos.chatwork.domain.model.room.{Room, RoomId}

import scala.concurrent.Future

private[message]
class AsyncMessageRepositoryOnV0Api
  extends AsyncMessageRepository {

  type This <: AsyncMessageRepositoryOnV0Api

  def say(text: Text)(room: Room)(implicit context: EntityIOContext[Future]): Future[Option[MessageId]] = {
    implicit val executor = getExecutionContext(context)
    SendChat.execute(SendChatRequest(text.text, room.roomId)) map {
      s =>
        s.messages.lastOption map { s => s.messageId }
    }
  }

  def messages(roomId: RoomId, from: MessageId, count: Int)(implicit context: EntityIOContext[Future]): Future[Seq[Message]] = {
    if (!from.roomId.equals(roomId)) {
      throw new IllegalArgumentException(s"Inconsistent roomId[$roomId] / messageId[${from.roomId}]")
    }
    implicit val executor = getExecutionContext(context)
    LoadOldChat.execute(LoadOldChatRequest(from)) map {
      response =>
        response.messages.take(count)
    }
  }

  def markAsRead(message: MessageId)(implicit context: EntityIOContext[Future]): Future[MessageId] = {
    implicit val executor = getExecutionContext(context)
    Read.execute(ReadRequest(message.roomId, message)) map {
      m =>
        message
    }
  }

  def resolve(identity: MessageId)(implicit context: EntityIOContext[Future]): Future[Message] = {
    implicit val executor = getExecutionContext(context)
    messages(identity.roomId, identity, 1).map {
      m =>
        m.find(_.identity.equals(identity)).get
    }
  }

  def containsByIdentity(identity: MessageId)(implicit context: EntityIOContext[Future]): Future[Boolean] = {
    implicit val executor = getExecutionContext(context)
    messages(identity.roomId, identity, 1).map {
      m =>
        m.exists(_.identity.equals(identity))
    }
  }
}
