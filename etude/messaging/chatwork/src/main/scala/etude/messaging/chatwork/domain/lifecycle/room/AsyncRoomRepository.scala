package etude.messaging.chatwork.domain.lifecycle.room

import scala.concurrent.Future
import etude.domain.core.lifecycle.async.AsyncEntityReader
import etude.messaging.chatwork.domain.model.room.{RoomIconGroup, RoomIcon, Room, RoomId}
import etude.domain.core.lifecycle.EntityIOContext
import etude.messaging.chatwork.domain.infrastructure.api.{AsyncEntityIOContextOnV1Api, AsyncEntityIOContextOnV0Api}

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
  def ofContext(context: EntityIOContext[Future]): AsyncRoomRepository = {
    context match {
      case c: AsyncEntityIOContextOnV0Api => ofV0Api()
      case c: AsyncEntityIOContextOnV1Api => ofV1Api()
      case _ => throw new IllegalArgumentException("Unsupported EntityIOContext")
    }
  }

  private def ofV0Api(): AsyncRoomRepository =
    new AsyncRoomRepositoryOnV0Api

  private def ofV1Api(): AsyncRoomRepository =
    new AsyncRoomRepositoryOnV1Api
}