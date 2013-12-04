package etude.chatwork.repository

import scala.util.Try
import etude.chatwork.model._
import etude.chatwork.model.RoomIconGroup

trait RoomRepository
  extends Repository[RoomId, Room]
  with EnumerableRepository[RoomId, Room] {

  def rooms(): Try[List[Room]]

  def create(name: String,
             roles: List[RoomRole],
             description: String = "",
             icon: RoomIcon = RoomIconGroup()): Try[RoomId]

  def asEntitiesList: Try[List[Room]] = rooms()

  def contains(entity: Room): Try[Boolean] = contains(entity.roomId)
}
