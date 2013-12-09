package etude.chatwork.domain.room

import scala.util.Try
import etude.commons.domain.Repository

trait RoomRoleRepository extends Repository[RoomRoleId, RoomRole] {
  def rolesInRoom(roomId: RoomId): Try[List[RoomRole]]

  def rolesInRoom(room: Room): Try[List[RoomRole]] = rolesInRoom(room.roomId)

  def updateRolesInRoom(roomRoles: List[RoomRole]): Try[List[RoomRole]]

  def contains(entity: RoomRole): Try[Boolean] = contains(entity.identity)
}
