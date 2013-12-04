package etude.chatwork.infrastructure.cache

import etude.chatwork.domain.room.{RoomId, RoomRole, RoomRoleId, RoomRoleRepository}
import scala.util.Try

case class CachedRoomRoleRepository(repository: RoomRoleRepository)
  extends RoomRoleRepository
  with CacheQoS[RoomRoleId, RoomRole] {

  def updateRolesInRoom(roomRoles: List[RoomRole]): Try[List[RoomRole]] = repository.updateRolesInRoom(roomRoles)

  def rolesInRoom(roomId: RoomId): Try[List[RoomRole]] = {
    entityListOperation(repository.rolesInRoom(roomId))
  }

  def resolve(identifier: RoomRoleId): Try[RoomRole] = {
    entityResolveOperation(identifier)(repository.resolve(identifier))
  }
}
