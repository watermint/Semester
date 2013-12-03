package etude.chatwork.v1

case class RoomRole(accountId: AccountId,
                    roomId: RoomId,
                    roleType: RoomRoleType)
  extends Entity[RoomRoleId]

