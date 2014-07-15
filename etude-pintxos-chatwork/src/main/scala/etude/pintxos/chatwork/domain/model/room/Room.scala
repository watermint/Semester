package etude.pintxos.chatwork.domain.model.room

import java.net.URI
import java.time.Instant

import etude.domain.core.lifecycle.EntityIOContext
import etude.domain.core.model.Entity
import etude.pintxos.chatwork.domain.lifecycle.message.MessageRepository
import etude.pintxos.chatwork.domain.model.message.{MessageId, Text}

import scala.language.higherKinds

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

  def say[M[+A]](text: Text)(implicit repository: MessageRepository[M], context: EntityIOContext[M]): M[Option[MessageId]] = {
    repository.say(text)(this)
  }
}
