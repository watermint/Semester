package etude.chatwork.domain.room

import etude.ddd.model.Entity

class RoomRole(val roomRoleId: RoomRoleId,
               val roleType: RoomRoleType)
  extends Entity[RoomRoleId] {

  val identity: RoomRoleId = roomRoleId
}
