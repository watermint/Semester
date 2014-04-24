package etude.messaging.chatwork.domain.lifecycle.room

import scala.concurrent.Future
import etude.domain.core.lifecycle.async.AsyncEntityReader
import etude.messaging.chatwork.domain.model.room.{RoomIconGroup, RoomIcon, Room, RoomId}
import etude.domain.core.lifecycle.EntityIOContext

trait AsyncRoomRepository
  extends RoomRepository[Future]
  with AsyncEntityReader[RoomId, Room] {

  type This <: AsyncRoomRepository

  def create(name: String,
             description: String = "",
             icon: RoomIcon = RoomIconGroup())
            (implicit context: EntityIOContext[Future]): Future[RoomId]
}

object AsyncRoomRepository {
  def ofV0Api(): AsyncRoomRepository =
    new AsyncRoomRepositoryOnV0Api

  def ofV1Api(): AsyncRoomRepository =
    new AsyncRoomRepositoryOnV1Api
}