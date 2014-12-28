package etude.vino.chatwork.historian.model

import org.json4s.JsonDSL._
import org.json4s._

case class RoomChunk(roomId: BigInt,
                     chunks: Seq[Chunk])
  extends Entity {

  def toJSON: JValue = {
    ("roomId" -> roomId) ~
      ("chunks" -> chunks.map(_.toJSON))
  }
}

object RoomChunk extends Parser[RoomChunk] {
  def fromJSON(json: JValue): RoomChunk = {
    (for {
      JObject(o) <- json
      JField("roomId", JInt(roomId)) <- o
      JField("chunks", JObject(chunks)) <- o
    } yield {
      RoomChunk(
        roomId,
        Chunk.ofChunks(chunks)
      )
    }).last
  }
}