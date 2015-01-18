package etude.vino.chatwork.domain.lifecycle

import etude.pintxos.chatwork.domain.model.account.AccountId
import etude.pintxos.chatwork.domain.model.room.{Participant, RoomId}
import etude.vino.chatwork.domain.infrastructure.ElasticSearch
import org.json4s.JsonDSL._
import org.json4s.{JArray, JField, JInt, JObject, JValue}

case class ParticipantRepository(engine: ElasticSearch) extends SimpleIndexRepository[Participant, RoomId] {

  val indexName: String = "cw-participant"

  val typeName: String = "participant"

  def fromJsonSeq(json: JValue): Seq[Participant] = {
    for {
      JObject(o) <- json
      JField("_source", JObject(source)) <- o
      JField("roomId", JInt(roomId)) <- source
      JField("admin", JArray(admin)) <- source
      JField("readonly", JArray(readonly)) <- source
      JField("member", JArray(member)) <- source
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
