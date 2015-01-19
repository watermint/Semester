package etude.vino.chatwork.domain.model

import etude.manieres.domain.model.{Identity, Entity}
import org.json4s.JsonDSL._
import org.json4s._



case class RoomChunk(roomId: RoomChunkId,
                     chunks: Seq[Chunk]) extends Entity[RoomChunkId] {

  val identity: RoomChunkId = roomId

  def toJSON: JValue = {
    ("roomId" -> roomId.value) ~
      ("chunks" -> chunks.map(_.toJSON))
  }
}

object RoomChunk {
  def fromJSON(json: JValue): RoomChunk = {
    (for {
      JObject(o) <- json
      JField("roomId", JInt(roomId)) <- o
      JField("chunks", JArray(chunks)) <- o
    } yield {
      RoomChunk(
        RoomChunkId(roomId),
        Chunk.ofChunks(chunks)
      )
    }).last
  }
}