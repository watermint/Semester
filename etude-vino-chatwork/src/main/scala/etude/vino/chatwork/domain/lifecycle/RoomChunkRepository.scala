package etude.vino.chatwork.domain.lifecycle

import etude.vino.chatwork.domain.infrastructure.ElasticSearch
import etude.vino.chatwork.domain.model.{Chunk, RoomChunk, RoomChunkId}
import org.json4s.JValue
import org.json4s.JsonAST.{JArray, JInt, JField, JObject}
import org.json4s.JsonDSL._

case class RoomChunkRepository(engine: ElasticSearch) extends SimpleIndexRepository[RoomChunk, RoomChunkId] {
  val indexName = "cw-historian-room"

  val typeName = "room-chunk"

  def fromJsonSeq(id: Option[String], source: JValue): Seq[RoomChunk] = {
    for {
      JObject(o) <- source
      JField("roomId", JInt(roomId)) <- o
      JField("chunks", JArray(chunks)) <- o
    } yield {
      RoomChunk(
        RoomChunkId(roomId),
        Chunk.ofChunks(chunks)
      )
    }
  }

  def toJson(entity: RoomChunk): JValue = {
    ("roomId" -> entity.roomId.value) ~
      ("chunks" -> entity.chunks.map(_.toJSON))
  }

  def toIdentity(identity: RoomChunkId): String = identity.value.toString()
}
