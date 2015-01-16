package etude.vino.chatwork.model.converter

import etude.pintxos.chatwork.domain.model.account.AccountId
import etude.pintxos.chatwork.domain.model.room.{RoomId, Participant}
import org.json4s.JsonDSL._
import org.json4s.{JField, JInt, JObject, JString, JValue, JArray}

object ParticipantConverter extends Converter {
  type E = Participant

  def fromJsonSeq(json: JValue): Seq[E] = {
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

  def toJson(entity: E): JValue = {
    ("roomId" -> entity.roomId.value) ~
      ("admin" -> entity.admin.map(_.value)) ~
      ("readonly" -> entity.readonly.map(_.value)) ~
      ("member" -> entity.member.map(_.value))
  }

  def toIdentity(entity: E): String = entity.roomId.value.toString()
}
