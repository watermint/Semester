package etude.vino.chatwork.domain.model

import etude.manieres.domain.model.Identity
import etude.pintxos.chatwork.domain.model.room.RoomId

case class RoomChunkId(roomId: BigInt) extends Identity[BigInt] {
  def value: BigInt = roomId
}

object RoomChunkId {
  def apply(roomId: RoomId): RoomChunkId = RoomChunkId(roomId.value)
}