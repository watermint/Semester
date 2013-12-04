package etude.chatwork.infrastructure.api.v0

import scala.util.{Success, Failure, Try}
import etude.chatwork.domain.message._
import etude.chatwork.infrastructure.api.NotImplementedException
import org.json4s._
import etude.chatwork.domain.room.RoomId
import etude.chatwork.domain.account.{Account, AccountId, AccountRepository}
import java.time.Instant

case class V0MessageRepository(implicit authToken: V0Api)
  extends MessageRepository {

  //  def messages(before: Message): List[Message] = {
  //    api(
  //      "load_old_chat",
  //      Map(
  //        "room_id" -> before.roomId.roomId,
  //        "first_chat_id" -> before.messageId.messageId
  //      )
  //    ) match {
  //      case Left(e) => throw e
  //      case Right(r) => {
  //        val result = r.asInstanceOf[Map[String, Any]]
  //        result.get("chat_list").get.asInstanceOf[List[Map[String, Any]]].map {
  //          c =>
  //            Message.fromChatList(before.roomId, c)
  //        }
  //      }
  //    }
  //  }

  //  def fromChatList(roomId: LegacyRoomId, chat: Map[String, Any]): LegacyMessage = {
  //    LegacyMessage(
  //      aid = LegacyAccountId(chat.get("aid").get.asInstanceOf[BigInt]),
  //      roomId = roomId,
  //      messageId = LegacyMessageId(chat.get("id").get.asInstanceOf[BigInt]),
  //      message = chat.get("msg").get.asInstanceOf[String],
  //      timestamp = Instant.ofEpochSecond(chat.get("tm").get.asInstanceOf[BigInt].longValue())
  //    )
  //  }

  protected def parseMessage(roomId: RoomId, json: JValue)(implicit accountRepository: AccountRepository): List[Message] = {
    for {
      JObject(data) <- json
      JField("aid", JInt(accountId)) <- data
      JField("id", JInt(messageId)) <- data
      JField("msg", JString(body)) <- data
      JField("tm", JInt(ctime)) <- data
    } yield {
      val account = accountRepository.resolve(new AccountId(accountId)) match {
        case Success(a) => a
        case Failure(f) => new Account(
          accountId = new AccountId(accountId),
          name = None,
          avatarImage = None
        )
      }

      new Message(
        messageId = new MessageId(roomId, messageId),
        account = account,
        body = body,
        ctime = Instant.ofEpochSecond(ctime.toLong),
        mtime = None
      )
    }
  }

  def messages(baseline: MessageId)(implicit accountRepository: AccountRepository): Try[List[Message]] = {
    authToken.api(
      "load_old_chat",
      Map(
        "room_id" -> baseline.roomId.value.toString(),
        "first_chat_id" -> baseline.messageId.toString()
      )
    ) match {
      case Success(json) => Success(parseMessage(baseline.roomId, json))
      case Failure(f) => Failure(f)
    }
  }

  def resolve(identifier: MessageId): Try[Message] = Failure(NotImplementedException("Migrated to v1 API"))
}
