package etude.chatwork.domain.room

import etude.foundation.domain.EntityNotFoundException

trait RoomRoleType

case class RoomRoleAdmin() extends RoomRoleType

case class RoomRoleMember() extends RoomRoleType

case class RoomRoleReadonly() extends RoomRoleType

object RoomRoleType {
  def apply(roleType: String): RoomRoleType = {
    roleType match {
      case "admin" => RoomRoleAdmin()
      case "member" => RoomRoleMember()
      case "readonly" => RoomRoleReadonly()
      case _ => throw EntityNotFoundException("No role type found for: " + roleType)
    }
  }
}