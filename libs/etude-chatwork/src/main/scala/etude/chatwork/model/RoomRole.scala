package etude.chatwork.model

import etude.commons.domain.Entity

class RoomRole(val roomRoleId: RoomRoleId,
               val roleType: RoomRoleType)
  extends Entity[RoomRoleId] {

  val identity: RoomRoleId = roomRoleId
}
