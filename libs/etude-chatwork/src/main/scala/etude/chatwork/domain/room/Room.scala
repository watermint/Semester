package etude.chatwork.domain.room

import java.net.URI
import java.time.Instant
import etude.ddd.model.Entity
import etude.chatwork.domain.account.AccountId
import scala.util.Success
import scala.util.Failure
import etude.chatwork.domain.JSONSerializable

/**
 * @see http://developer.chatwork.com/ja/endpoint_rooms.html#GET-rooms
 */
class Room(val roomId: RoomId,
           val name: String,
           val description: Option[String],
           val attributes: Option[RoomAttributes],
           val roomType: RoomType,
           val roomRole: RoomRoleType,
           val avatar: URI,
           val lastUpdateTime: Instant)
  extends Entity[RoomId]
  with JSONSerializable {

  val identity: RoomId = roomId

  def copy(name: String = this.name,
           description: Option[String] = this.description,
           attributes: Option[RoomAttributes] = this.attributes): Room = {
    new Room(
      roomId = this.roomId,
      name = name,
      description = description,
      attributes = attributes,
      roomType = this.roomType,
      roomRole = this.roomRole,
      avatar = this.avatar,
      lastUpdateTime = this.lastUpdateTime
    )
  }

  def roles(implicit roomRoleRepository: RoomRoleRepository): List[RoomRole] = {
    roomRoleRepository.rolesInRoom(this) match {
      case Failure(f) => throw f
      case Success(roles) => roles
    }
  }

  def attendees(implicit roomRoleRepository: RoomRoleRepository): List[AccountId] = {
    roles(roomRoleRepository).map(_.roomRoleId.accountId).toList
  }
}
