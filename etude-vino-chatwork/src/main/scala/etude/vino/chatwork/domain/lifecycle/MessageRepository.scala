package etude.vino.chatwork.domain.lifecycle

import java.time.{ZoneOffset, Instant}

import etude.pintxos.chatwork.domain.model.account.AccountId
import etude.pintxos.chatwork.domain.model.message.{Message, MessageId, Text}
import etude.pintxos.chatwork.domain.model.room.RoomId
import org.json4s.JsonDSL._
import org.json4s.{JField, JInt, JObject, JString, JValue}

object MessageRepository extends Repository[Message] {

  def indexName(entity: Message): String = {
    val indexDate = entity.ctime.atOffset(ZoneOffset.UTC).getYear
    s"cw-message-$indexDate"
  }

  def typeName(entity: Message): String = "message"

  def fromJsonSeq(json: JValue): Seq[Message] = {
    for {
      JObject(o) <- json
      JField("_source", JObject(source)) <- o
      JField("_id", JString(id)) <- o
      JField("@timestamp", JString(timestamp)) <- source
      JField("account", JInt(accountId)) <- source
      JField("body", JString(body)) <- source
    } yield {
      val idPair = id.split("-")
      new Message(
        MessageId(RoomId(BigInt(idPair(0))), BigInt(idPair(1))),
        AccountId(accountId),
        Text(body),
        Instant.parse(timestamp),
        None
      )
    }
  }

  def toJson(entity: Message): JValue = {
    val toAccount = entity.body.to.map(_.value)
    val replies = entity.body.replyTo.map(_.value)

    ("@timestamp" -> entity.ctime.toString) ~
      ("body" -> entity.body.text) ~
      ("account" -> entity.accountId.value) ~
      ("room" -> entity.messageId.roomId.value) ~
      ("to" -> toAccount.toList) ~
      ("replyTo" -> replies)
  }

  def toIdentity(entity: Message): String = s"${entity.messageId.roomId.value}-${entity.messageId.messageId}"
}
