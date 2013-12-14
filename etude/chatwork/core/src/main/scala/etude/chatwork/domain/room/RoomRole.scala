package etude.chatwork.domain.room

import etude.chatwork.domain.JSONSerializable
import etude.foundation.domain.Entity

class RoomRole(val roomRoleId: RoomRoleId,
               val roleType: RoomRoleType)
  extends Entity[RoomRoleId]
  with JSONSerializable {

  val identity: RoomRoleId = roomRoleId
}
