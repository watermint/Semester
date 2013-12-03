package etude.chatwork.v0

case class RoomId(roomId: String) {
  lazy val id: BigInt = BigInt(roomId)
}

object RoomId {
  def apply(roomId: BigInt): RoomId = RoomId(roomId.toString())
}