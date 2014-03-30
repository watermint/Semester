package etude.messaging.chatwork.domain.room

import java.net.URI
import java.time.Instant
import etude.foundation.domain.model.Entity

/**
 * @see http://developer.chatwork.com/ja/endpoint_rooms.html#GET-rooms
 */
class Room(val roomId: RoomId,
           val name: String,
           val description: Option[String],
           val attributes: Option[RoomAttributes],
           val roomType: RoomType,
           val avatar: Option[URI],
           val lastUpdateTime: Option[Instant])
  extends Entity[RoomId] {

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
      avatar = this.avatar,
      lastUpdateTime = this.lastUpdateTime
    )
  }
}
