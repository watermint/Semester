package etude.chatwork.v1

import scala.util.Try

trait RoomRepository
  extends Repository[RoomId, Room]
  with EnumerableRepository[RoomId, Room] {

  def rooms(): Try[List[Room]]

  def asEntitiesList: Try[List[Room]] = rooms()

  def contains(entity: Room): Try[Boolean] = contains(entity.roomId)
}
