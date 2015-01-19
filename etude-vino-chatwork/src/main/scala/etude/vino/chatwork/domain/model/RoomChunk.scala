package etude.vino.chatwork.domain.model

import etude.manieres.domain.model.Entity
import etude.pintxos.chatwork.domain.model.room.RoomId

case class RoomChunk(roomId: RoomId,
                     chunks: Seq[Chunk]) extends Entity[RoomId] {

  val identity: RoomId = roomId
}

