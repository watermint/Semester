package etude.vino.chatwork.domain.lifecycle

import etude.pintxos.chatwork.domain.model.account.AccountId
import etude.pintxos.chatwork.domain.model.room.{Participant, RoomId}
import etude.vino.chatwork.domain.infrastructure.ElasticSearch
import org.json4s.JsonDSL._
import org.json4s.{JArray, JField, JInt, JObject, JValue}

case class ParticipantRepository(engine: ElasticSearch) extends SimpleIndexRepository[Participant, RoomId] {

  val indexName: String = "cw-participant"

  val typeName: String = "participant"

  def fromJsonSeq(id: String, source: JValue): Seq[Participant] = {
    for {
      JObject(o) <- source
      JField("roomId", JInt(roomId)) <- o
      JField("admin", JArray(admin)) <- o
      JField("readonly", JArray(readonly)) <- o
      JField("member", JArray(member)) <- o
    } yield {
      val adminIds = for { JInt(a) <- admin } yield { AccountId(a) }
      val readonlyIds = for { JInt(r) <- readonly } yield { AccountId(r) }
      val memberIds = for { JInt(m) <- member } yield { AccountId(m) }

      new Participant(
        roomId = RoomId(roomId),
        admin = adminIds,
        member = memberIds,
        readonly = readonlyIds
      )
    }
  }

  def toJson(entity: Participant): JValue = {
    ("roomId" -> entity.roomId.value) ~
      ("admin" -> entity.admin.map(_.value)) ~
      ("readonly" -> entity.readonly.map(_.value)) ~
      ("member" -> entity.member.map(_.value))
  }

  def toIdentity(identity: RoomId): String = identity.value.toString()
}
