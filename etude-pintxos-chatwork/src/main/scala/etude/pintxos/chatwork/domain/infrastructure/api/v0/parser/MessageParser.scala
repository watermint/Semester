package etude.pintxos.chatwork.domain.infrastructure.api.v0.parser

import java.time.Instant

import etude.pintxos.chatwork.domain.model.account.AccountId
import etude.pintxos.chatwork.domain.model.message.{Text, MessageId, Message}
import etude.pintxos.chatwork.domain.model.room.RoomId
import org.json4s.JsonAST.{JString, JInt, JField, JObject}
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
