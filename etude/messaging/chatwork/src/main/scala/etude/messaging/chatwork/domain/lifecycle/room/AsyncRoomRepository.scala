package etude.messaging.chatwork.domain.lifecycle.room

import scala.concurrent.Future
import etude.foundation.domain.lifecycle.async.AsyncEntityReader
import etude.messaging.chatwork.domain.model.room.{Room, RoomId}

trait AsyncRoomRepository
  extends RoomRepository[Future]
  with AsyncEntityReader[RoomId, Room] {

  type This <: AsyncRoomRepository
}
