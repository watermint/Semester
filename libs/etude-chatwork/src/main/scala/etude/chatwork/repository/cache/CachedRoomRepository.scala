package etude.chatwork.repository.cache

import scala.util.Try
import scala.util.Failure
import scala.util.Success
import etude.chatwork.model.{RoomRole, RoomId, RoomIcon, Room}
import etude.chatwork.repository.RoomRepository
import etude.chatwork.repository.api.v1.ApiRoomRepository

case class CachedRoomRepository(api: ApiRoomRepository)
  extends RoomRepository
  with CacheQoS[RoomId, Room] {

  def rooms(): Try[List[Room]] = {
    entityListOperation(api.rooms())
  }

  def create(name: String, roles: List[RoomRole], description: String, icon: RoomIcon): Try[RoomId] = {
    api.create(name, roles, description, icon) match {
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
    entityResolveOperation(identifier)(api.resolve(identifier))
  }
}
