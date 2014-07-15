package etude.kitchenette.fedelini.provisioning

import etude.pintxos.chatwork.domain.model.room.AccountRoleType

object ProvisioningPolicy {

  def toRoleThenFromRole(mapping: RoomRoleMapping): Option[AccountRoleType] = {
    mapping.toRoomRole match {
      case Some(t) =>
        mapping.toRoomRole
      case _ =>
        mapping.fromRoomRole
    }
  }

  def toRoleThenSpecifiedRole(mapping: RoomRoleMapping)(role: AccountRoleType): Option[AccountRoleType] = {
    mapping.toRoomRole match {
      case Some(t) =>
        mapping.toRoomRole
      case _ =>
        Some(role)
    }
  }

  def toRoleThenAdmin(mapping: RoomRoleMapping): Option[AccountRoleType] = toRoleThenSpecifiedRole(mapping)(AccountRoleType.Admin)

  def toRoleThenMember(mapping: RoomRoleMapping): Option[AccountRoleType] = toRoleThenSpecifiedRole(mapping)(AccountRoleType.Member)

  def toRoleThenReadonly(mapping: RoomRoleMapping): Option[AccountRoleType] = toRoleThenSpecifiedRole(mapping)(AccountRoleType.Readonly)
}
