package etude.chatwork.v0

case class GroupId(groupId: String) {
  lazy val id: BigInt = BigInt(groupId)
}

object GroupId {
  lazy val EMPTY = GroupId("0")

  def apply(groupId: BigInt): GroupId = GroupId(groupId.toString())
}