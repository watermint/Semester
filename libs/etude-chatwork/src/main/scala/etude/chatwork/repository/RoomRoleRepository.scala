package etude.chatwork.repository

import scala.util.Try
import etude.chatwork.model.{RoomRoleId, RoomRole, RoomId, Room}

trait RoomRoleRepository extends Repository[RoomRoleId, RoomRole] {
  def rolesInRoom(roomId: RoomId): Try[List[RoomRole]]

  def rolesInRoom(room: Room): Try[List[RoomRole]] = rolesInRoom(room.roomId)

  def updateRolesInRoom(roomRoles: List[RoomRole]): Try[List[RoomRole]]

  def contains(entity: RoomRole): Try[Boolean] = contains(entity.identity)
}
