package etude.chatwork.domain.room

import etude.foundation.domain.lifecycle.async.AsyncFactory
import scala.concurrent.Future
import etude.foundation.domain.lifecycle.EntityIOContext

trait AsyncRoomFactory
  extends RoomFactory[Future]
  with AsyncFactory {

  type This <: AsyncRoomFactory

  def create(name: String,
             description: String = "",
             icon: RoomIcon = RoomIconGroup())
            (implicit context: EntityIOContext[Future]): Future[RoomId]
}
