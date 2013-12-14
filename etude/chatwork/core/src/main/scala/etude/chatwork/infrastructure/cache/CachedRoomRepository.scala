package etude.chatwork.infrastructure.cache

import scala.util.Try
import etude.chatwork.domain.room._
import scala.util.Failure
import scala.util.Success
import etude.chatwork.domain.message.MessageId

case class CachedRoomRepository(repository: RoomRepository)
  extends RoomRepository
  with CacheQoS[RoomId, Room] {

  def latestMessage(roomId: RoomId): Try[MessageId] = repository.latestMessage(roomId)

  def markAsRead(message: MessageId): Try[MessageId] = repository.markAsRead(message)

  def rooms(): Try[List[Room]] = {
    entityListOperation(repository.rooms())
  }

  def create(name: String, roles: List[RoomRole], description: String, icon: RoomIcon): Try[RoomId] = {
    repository.create(name, roles, description, icon) match {
      case Failure(f) => Failure(f)
      case Success(rid) =>
        resolve(rid) match {
          case Success(r) => cacheEntity(r)
          case _ =>
        }
        Success(rid)
    }
  }

  def resolve(identifier: RoomId): Try[Room] = {
    entityResolveOperation(identifier)(repository.resolve(identifier))
  }
}
