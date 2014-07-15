package etude.pintxos.chatwork.domain.lifecycle.message

import java.time.Instant

import etude.domain.core.lifecycle.async.AsyncEntityIO
import etude.domain.core.lifecycle.{EntityIOContext, EntityNotFoundException}
import etude.pintxos.chatwork.domain.infrastructure.api.v1.{V1ApiException, V1AsyncApi}
import etude.pintxos.chatwork.domain.model.account.AccountId
import etude.pintxos.chatwork.domain.model.message.{Message, MessageId, Text}
import etude.pintxos.chatwork.domain.model.room.{Room, RoomId}
import org.json4s._

import scala.concurrent._

private[message]
class AsyncMessageRepositoryOnV1Api
  extends AsyncMessageRepository
  with AsyncEntityIO {

  type This <: AsyncMessageRepositoryOnV1Api

  private def parseMessages(roomId: RoomId, json: JValue): List[Message] = {
    for {
      JObject(data) <- json
      JField("message_id", JInt(messageId)) <- data
      JField("account", JObject(account)) <- data
      JField("account_id", JInt(accountId)) <- account
      JField("name", JString(accountName)) <- account
      JField("avatar_image_url", JString(accountIcon)) <- account
      JField("body", JString(body)) <- data
      JField("send_time", JInt(ctime)) <- data
      JField("update_time", JInt(mtime)) <- data
    } yield {
      new Message(
        messageId = MessageId(roomId, messageId),
        accountId = AccountId(accountId),
        body = Text(body),
        ctime = Instant.ofEpochSecond(ctime.toLong),
        mtime = mtime.toLong match {
          case 0 => None
          case t => Some(Instant.ofEpochSecond(t))
        }
      )
    }
  }

  def say(text: Text)(room: Room)(implicit context: EntityIOContext[Future]): Future[Option[MessageId]] = {
    implicit val executor = getExecutionContext(context)
    val endPoint = s"/v1/rooms/${room.roomId.value}/messages"
    V1AsyncApi.post(endPoint, data = Map("body" -> text.text)) map {
      json =>
        val JInt(messageId) = json \ "message_id"
        Some(MessageId(room.identity, messageId))
    }
  }

  def markAsRead(message: MessageId)(implicit context: EntityIOContext[Future]): Future[MessageId] =
    Future.failed(new UnsupportedOperationException("Mark as read is not yet supported."))

  def messages(roomId: RoomId, from: MessageId, count: Int)(implicit context: EntityIOContext[Future]): Future[List[Message]] =
    Future.failed(new UnsupportedOperationException("see http://developer.chatwork.com/ja/endpoint_rooms.html#GET-rooms-room_id-messages"))

  def containsByIdentity(identity: MessageId)(implicit context: EntityIOContext[Future]): Future[Boolean] = {
    implicit val executor = getExecutionContext(context)
    val endPoint = s"/v1/rooms/${identity.roomId.value}/messages/${identity.messageId}"

    V1AsyncApi.get(endPoint) map {
      json =>
        true
    } recover {
      case e: V1ApiException => false
    }
  }

  def resolve(identifier: MessageId)(implicit context: EntityIOContext[Future]): Future[Message] = {
    implicit val executor = getExecutionContext(context)
    val endPoint = s"/v1/rooms/${identifier.roomId.value}/messages/${identifier.messageId}"

    V1AsyncApi.get(endPoint) map {
      json =>
        parseMessages(identifier.roomId, json).lastOption match {
          case Some(m) => m
          case _ => throw EntityNotFoundException(s"Message not found in room (${identifier.roomId.value}) of message id (${identifier.messageId}")
        }
    }
  }
}
