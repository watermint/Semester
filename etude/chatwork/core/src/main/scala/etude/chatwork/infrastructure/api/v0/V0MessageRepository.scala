package etude.chatwork.infrastructure.api.v0

import etude.chatwork.domain.message._
import etude.chatwork.infrastructure.api.NotImplementedException
import org.json4s._
import etude.chatwork.domain.room.RoomId
import etude.chatwork.domain.account.AccountId
import java.time.Instant
import scala.concurrent._
import etude.foundation.domain.lifecycle.EntityIOContext

class V0MessageRepository
  extends AsyncMessageRepository {

  type This <: V0MessageRepository

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

  def messages(baseline: MessageId)(implicit context: EntityIOContext[Future]): Future[List[Message]] = {
    implicit val executor = getExecutionContext(context)
    V0AsyncApi.api(
      "load_old_chat",
      Map(
        "room_id" -> baseline.roomId.value.toString(),
        "first_chat_id" -> baseline.messageId.toString()
      )
    ) map {
      json =>
        parseMessage(baseline.roomId, json)
    }
  }

  def resolve(identity: MessageId)(implicit context: EntityIOContext[Future]): Future[Message] =
    Future.failed(NotImplementedException("Migrated to v1 API"))

  def containsByIdentity(identity: MessageId)(implicit context: EntityIOContext[Future]): Future[Boolean] =
    Future.failed(NotImplementedException("Migrated to v1 API"))
}
