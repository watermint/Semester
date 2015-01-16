package etude.vino.chatwork.model.converter

import java.time.Instant

import etude.pintxos.chatwork.domain.model.account.AccountId
import etude.pintxos.chatwork.domain.model.message.{Text, Message, MessageId}
import etude.pintxos.chatwork.domain.model.room.RoomId
import org.json4s.JsonDSL._
import org.json4s.{JField, JInt, JObject, JString, JValue}

object MessageConverter extends Converter {
  type E = Message

  def fromJsonSeq(json: JValue): Seq[E] = {
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

  def toJson(entity: E): JValue = {
    val toAccount = entity.body.to.map(_.value)
    val replies = entity.body.replyTo.map(_.value)

    ("@timestamp" -> entity.ctime.toString) ~
      ("body" -> entity.body.text) ~
      ("account" -> entity.accountId.value) ~
      ("room" -> entity.messageId.roomId.value) ~
      ("to" -> toAccount.toList) ~
      ("replyTo" -> replies)
  }

  def toIdentity(entity: E): String = s"${entity.messageId.roomId.value}-${entity.messageId.messageId}"
}
