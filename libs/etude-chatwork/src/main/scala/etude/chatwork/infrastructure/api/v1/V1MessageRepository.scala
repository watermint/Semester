package etude.chatwork.infrastructure.api.v1

import etude.chatwork.domain.message.{Message, MessageId, MessageRepository}
import etude.chatwork.domain.room.RoomId
import scala.util.{Success, Failure, Try}
import etude.chatwork.infrastructure.api.{QoSException, ApiQoS, TokenAuthentication, NotImplementedException}
import org.json4s._
import etude.chatwork.domain.account.{AccountRepository, AccountId, Account}
import java.net.URI
import java.time.Instant
import etude.chatwork.domain.EntityNotFoundException

case class V1MessageRepository(implicit authToken: TokenAuthentication)
  extends MessageRepository
  with ApiQoS {

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
        messageId = new MessageId(roomId, messageId),
        account = new Account(
          accountId = new AccountId(accountId),
          name = Some(accountName),
          avatarImage = Some(new URI(accountIcon))
        ),
        body = body,
        ctime = Instant.ofEpochSecond(ctime.toLong),
        mtime = mtime.toLong match {
          case 0 => None
          case t => Some(Instant.ofEpochSecond(t))
        }
      )
    }
  }

  def messages(baseline: MessageId)(implicit accountRepository: AccountRepository): Try[List[Message]] =
    Failure(NotImplementedException("see http://developer.chatwork.com/ja/endpoint_rooms.html#GET-rooms-room_id-messages"))

  def resolve(identifier: MessageId): Try[Message] = {
    val endPoint = s"/v1/rooms/${identifier.roomId.value}/messages/${identifier.messageId}"
    val operation = s"GET $endPoint"
    if (shouldFail(operation)) {
      return Failure(QoSException(operation))
    }

    V1Api.get(endPoint) match {
      case Failure(f) => Failure(f)
      case Success(json) =>
        parseMessages(identifier.roomId, json).lastOption match {
          case Some(m) => Success(m)
          case _ => Failure(EntityNotFoundException(s"Message not found in room (${identifier.roomId.value}) of message id (${identifier.messageId}"))
        }
    }
  }
}
