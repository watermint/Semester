package semester.application.vino.domain.model

import semester.foundation.domain.model.Entity
import semester.service.chatwork.domain.model.room.RoomId

case class RoomChunk(roomId: RoomId,
                     chunks: Seq[Chunk]) extends Entity[RoomId] {

  val identity: RoomId = roomId
}

