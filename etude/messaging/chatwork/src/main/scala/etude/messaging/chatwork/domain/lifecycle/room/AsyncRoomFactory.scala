package etude.messaging.chatwork.domain.lifecycle.room

import etude.foundation.domain.lifecycle.async.AsyncFactory
import scala.concurrent.Future
import etude.foundation.domain.lifecycle.EntityIOContext
import etude.messaging.chatwork.domain.model.room.{RoomId, RoomIconGroup, RoomIcon, RoomFactory}

trait AsyncRoomFactory
  extends RoomFactory[Future]
  with AsyncFactory {

  type This <: AsyncRoomFactory

  def create(name: String,
             description: String = "",
             icon: RoomIcon = RoomIconGroup())
            (implicit context: EntityIOContext[Future]): Future[RoomId]
}
