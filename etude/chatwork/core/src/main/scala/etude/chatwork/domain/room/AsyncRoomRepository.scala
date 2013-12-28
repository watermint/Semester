package etude.chatwork.domain.room

import scala.concurrent.Future
import etude.foundation.domain.lifecycle.async.AsyncEntityReader

trait AsyncRoomRepository
  extends RoomRepository[Future]
  with AsyncEntityReader[RoomId, Room] {

  type This <: AsyncRoomRepository
}
