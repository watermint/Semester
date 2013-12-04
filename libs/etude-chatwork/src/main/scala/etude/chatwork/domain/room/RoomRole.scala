package etude.chatwork.domain.room

import etude.ddd.model.Entity
import etude.chatwork.domain.JSONSerializable

class RoomRole(val roomRoleId: RoomRoleId,
               val roleType: RoomRoleType)
  extends Entity[RoomRoleId]
  with JSONSerializable {

  val identity: RoomRoleId = roomRoleId
}
