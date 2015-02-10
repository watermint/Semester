package semester.application.vino.domain.lifecycle

import semester.service.chatwork.domain.model.room.RoomId
import semester.application.vino.domain.infrastructure.ElasticSearch
import semester.application.vino.domain.model.{Chunk, RoomChunk}
import org.json4s.JValue
import org.json4s.JsonAST.{JArray, JField, JInt, JObject}
import org.json4s.JsonDSL._

case class RoomChunkRepository(engine: ElasticSearch) extends SimpleIndexRepository[RoomChunk, RoomId] {
  val indexName = "cw-historian-room"

  val typeName = "room-chunk"

  def fromJsonSeq(id: Option[String], source: JValue): Seq[RoomChunk] = {
    for {
      JObject(o) <- source
      JField("roomId", JInt(roomId)) <- o
      JField("chunks", JArray(chunks)) <- o
    } yield {
      RoomChunk(
        RoomId(roomId),
        Chunk.ofChunks(chunks)
      )
    }
  }

  def toJson(entity: RoomChunk): JValue = {
    ("roomId" -> entity.roomId.value) ~
      ("chunks" -> entity.chunks.map(_.toJSON))
  }

  def toIdentity(identity: RoomId): String = identity.value.toString()
}
