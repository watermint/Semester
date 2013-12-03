package etude.chatwork.v1

import scala.util.Try

trait RoomRoleRepository extends Repository[RoomRoleId, RoomRole] {
  def rolesInRoom(roomId: RoomId): Try[List[RoomRole]]

  def updateRolesInRoom(roomRoles: List[RoomRole]): Try[List[RoomRole]]

  def contains(entity: RoomRole): Try[Boolean] = contains(RoomRoleId(entity.accountId, entity.roomId))
}
