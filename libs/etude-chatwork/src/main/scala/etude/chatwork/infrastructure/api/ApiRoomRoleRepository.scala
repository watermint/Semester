package etude.chatwork.infrastructure.api

import etude.chatwork.domain.room.{RoomId, RoomRole, RoomRoleId, RoomRoleRepository}
import scala.util.Try
import etude.chatwork.infrastructure.api.v1.V1RoomRoleRepository

case class ApiRoomRoleRepository(implicit authContext: MixedAuthContext)
  extends RoomRoleRepository {
  def rolesInRoom(roomId: RoomId): Try[List[RoomRole]] =
    V1RoomRoleRepository().rolesInRoom(roomId)

  def updateRolesInRoom(roomRoles: List[RoomRole]): Try[List[RoomRole]] =
    V1RoomRoleRepository().updateRolesInRoom(roomRoles)

  def resolve(identifier: RoomRoleId): Try[RoomRole] =
    V1RoomRoleRepository().resolve(identifier)
}
