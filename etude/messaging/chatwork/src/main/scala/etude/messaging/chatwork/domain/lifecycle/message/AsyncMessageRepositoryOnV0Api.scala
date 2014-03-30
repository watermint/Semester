package etude.messaging.chatwork.domain.lifecycle.message

import etude.messaging.chatwork.domain.model.message._
import org.json4s._
import etude.messaging.chatwork.domain.model.room.{Room, RoomId}
import etude.messaging.chatwork.domain.model.account.AccountId
import java.time.Instant
import scala.concurrent._
import etude.foundation.domain.lifecycle.EntityIOContext
import etude.messaging.chatwork.domain.infrastructure.NotImplementedException
import etude.messaging.chatwork.domain.infrastructure.v0.V0AsyncApi

class AsyncMessageRepositoryOnV0Api
  extends AsyncMessageRepository {

  type This <: AsyncMessageRepositoryOnV0Api

  protected def parseMessage(roomId: RoomId, json: JValue): List[Message] = {
    for {
      JObject(data) <- json
      JField("aid", JInt(accountId)) <- data
      JField("id", JInt(messageId)) <- data
      JField("msg", JString(body)) <- data
      JField("tm", JInt(ctime)) <- data
    } yield {
      new Message(
        messageId = new MessageId(roomId, messageId),
        accountId = new AccountId(accountId),
        body = body,
        ctime = Instant.ofEpochSecond(ctime.toLong),
        mtime = None
      )
    }
  }

  def say(text: Text)(room: Room)(implicit context: EntityIOContext[Future]): Future[MessageId] = ???

  def messages(roomId: RoomId, from: MessageId, count: Int)(implicit context: EntityIOContext[Future]): Future[List[Message]] = {
    if (!from.roomId.equals(roomId)) {
      throw new IllegalArgumentException(s"Inconsistent roomId[$roomId] / messageId[${from.roomId}]")
    }
    implicit val executor = getExecutionContext(context)
    V0AsyncApi.api(
      "load_old_chat",
      Map(
        "room_id" -> from.roomId.value.toString(),
        "first_chat_id" -> from.messageId.toString()
      )
    ) map {
      json =>
        parseMessage(from.roomId, json).take(count)
    }
  }

  def markAsRead(message: MessageId)(implicit context: EntityIOContext[Future]): Future[MessageId] = {
    implicit val executor = getExecutionContext(context)
    V0AsyncApi.api(
      "read",
      Map(
        "room_id" -> message.roomId.value.toString(),
        "last_chat_id" -> message.messageId.toString()
      )
    ) map {
      json =>
        message
    }
  }

  def resolve(identity: MessageId)(implicit context: EntityIOContext[Future]): Future[Message] =
    Future.failed(NotImplementedException("Migrated to v1 API"))

  def containsByIdentity(identity: MessageId)(implicit context: EntityIOContext[Future]): Future[Boolean] =
    Future.failed(NotImplementedException("Migrated to v1 API"))
}
