package semester.service.chatwork.domain.service.v0.parser

import java.time.Instant

import semester.service.chatwork.domain.model.account.AccountId
import semester.service.chatwork.domain.model.message.{Message, MessageId, Text}
import semester.service.chatwork.domain.model.room.RoomId
import org.json4s.JsonAST.{JField, JInt, JObject, JString}
import org.json4s._

object MessageParser extends ParserBase {

  def parseMessage(roomId: RoomId, json: JValue): Seq[Message] = {
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
        body = Text(body),
        ctime = Instant.ofEpochSecond(ctime.toLong),
        mtime = None
      )
    }
  }
}
