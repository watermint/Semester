package etude.vino.chatwork.domain.model

import etude.manieres.domain.model.Entity
import etude.pintxos.chatwork.domain.model.room.RoomId
import org.json4s.JsonDSL._
import org.json4s._



case class RoomChunk(roomId: RoomId,
                     chunks: Seq[Chunk]) extends Entity[RoomId] {

  val identity: RoomId = roomId

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
        RoomId(roomId),
        Chunk.ofChunks(chunks)
      )
    }).last
  }
}