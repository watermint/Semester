package etude.chatwork.infrastructure.api.v1

import etude.chatwork.domain.message.{AsyncMessageRepository, Message, MessageId}
import etude.chatwork.domain.room.RoomId
import etude.chatwork.infrastructure.api.{ApiQoS, NotImplementedException}
import org.json4s._
import etude.chatwork.domain.account.AccountId
import java.time.Instant
import etude.foundation.domain.lifecycle.{EntityIOContext, EntityNotFoundException}
import scala.concurrent._
import etude.foundation.domain.lifecycle.async.AsyncEntityIO

class V1MessageRepository
  extends AsyncMessageRepository
  with AsyncEntityIO
  with ApiQoS {

  type This <: V1MessageRepository

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
        body = body,
        ctime = Instant.ofEpochSecond(ctime.toLong),
        mtime = mtime.toLong match {
          case 0 => None
          case t => Some(Instant.ofEpochSecond(t))
        }
      )
    }
  }
  def messages(baseline: MessageId): Future[List[Message]] =
    Future.failed(NotImplementedException("see http://developer.chatwork.com/ja/endpoint_rooms.html#GET-rooms-room_id-messages"))

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
