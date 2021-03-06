package semester.application.vino.domain.lifecycle

import semester.service.chatwork.domain.model.room.RoomId
import semester.application.vino.domain.infrastructure.ElasticSearch
import semester.application.vino.domain.model.MarkAsRead
import org.json4s.JsonAST.JString
import org.json4s.JsonDSL._
import org.json4s.{JBool, JField, JObject, JValue}

case class MarkAsReadRepository(engine: ElasticSearch) extends SimpleIndexRepository[MarkAsRead, RoomId] {
  val indexName = "cw-markasread"

  val typeName = "markasread"

  def fromJsonSeq(id: Option[String], source: JValue): Seq[MarkAsRead] = {
    for {
      JObject(o) <- source
      JField("roomId", JString(roomId)) <- o // JString instead of JInt due to compatibility issue
      JField("markasread", JBool(markasread)) <- o
    } yield {
      MarkAsRead(
        RoomId(BigInt(roomId)),
        markasread
      )
    }
  }

  def toJson(entity: MarkAsRead): JValue = {
    ("roomId" -> entity.roomId.value.toString()) ~
      ("markasread" -> true)
  }

  def toIdentity(identity: RoomId): String = identity.value.toString()
}
