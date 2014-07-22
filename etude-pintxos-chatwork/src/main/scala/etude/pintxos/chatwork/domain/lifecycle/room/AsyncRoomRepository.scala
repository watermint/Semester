package etude.pintxos.chatwork.domain.lifecycle.room

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.manieres.domain.lifecycle.async.AsyncEntityReader
import etude.pintxos.chatwork.domain.infrastructure.api.{AsyncEntityIOContextOnV0Api, AsyncEntityIOContextOnV1Api}
import etude.pintxos.chatwork.domain.model.room.{Room, RoomIcon, RoomIconGroup, RoomId}

import scala.concurrent.Future

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