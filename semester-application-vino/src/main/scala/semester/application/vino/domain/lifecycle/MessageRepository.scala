package semester.application.vino.domain.lifecycle

import java.time.{Instant, ZoneOffset}

import semester.service.chatwork.domain.model.account.AccountId
import semester.service.chatwork.domain.model.message.{Message, MessageId, Text}
import semester.service.chatwork.domain.model.room.RoomId
import semester.application.vino.domain.infrastructure.ElasticSearch
import org.json4s.JsonDSL._
import org.json4s.{JField, JInt, JObject, JString, JValue}

case class MessageRepository(engine: ElasticSearch) extends MultiIndexRepository[Message, MessageId] {

  val indexNamePrefix: String = "cw-message-"

  def indexName(entity: Message): String = {
    val indexDate = entity.ctime.atOffset(ZoneOffset.UTC).getYear
    s"$indexNamePrefix$indexDate"
  }

  val typeName: String = "message"

  def fromJsonSeq(id: Option[String], source: JValue): Seq[Message] = {
    for {
      JObject(o) <- source
      JField("@timestamp", JString(timestamp)) <- o
      JField("account", JInt(accountId)) <- o
      JField("body", JString(body)) <- o
    } yield {
      val idPair = id.get.split("-")
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

  def toIdentity(identity: MessageId): String = s"${identity.roomId.value}-${identity.messageId}"
}
