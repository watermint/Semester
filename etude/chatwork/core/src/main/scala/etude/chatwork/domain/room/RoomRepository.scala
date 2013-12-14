package etude.chatwork.domain.room

import scala.util.Try
import etude.chatwork.domain.message.MessageId
import etude.foundation.domain.{Repository, EnumerableRepository}

trait RoomRepository
  extends Repository[RoomId, Room]
  with EnumerableRepository[RoomId, Room] {

  def rooms(): Try[List[Room]]

  def create(name: String,
             roles: List[RoomRole],
             description: String = "",
             icon: RoomIcon = RoomIconGroup()): Try[RoomId]

  def latestMessage(roomId: RoomId): Try[MessageId]

  def markAsRead(message: MessageId): Try[MessageId]

  def asEntitiesList: Try[List[Room]] = rooms()

  def contains(entity: Room): Try[Boolean] = contains(entity.roomId)
}
