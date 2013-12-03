package etude.chatwork.v1

case class RoomRoleId(accountId: AccountId,
                      roomId: RoomId)
  extends Id[RoomRoleId, RoomRole]
