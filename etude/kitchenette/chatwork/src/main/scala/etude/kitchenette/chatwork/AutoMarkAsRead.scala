package etude.kitchenette.chatwork

import scala.concurrent.ExecutionContext.Implicits.global
import etude.messaging.chatwork.domain.model.room.{Room, RoomId}
import etude.messaging.chatwork.domain.infrastructure.api.AsyncEntityIOContextOnV0Api
import etude.messaging.chatwork.domain.lifecycle.room.AsyncRoomRepository
import etude.messaging.chatwork.domain.lifecycle.message.AsyncMessageRepository
import scala.concurrent.Future
import etude.messaging.chatwork.domain.event.message.AsyncMessageEventPublisher
import com.twitter.logging.Logger

case class AutoMarkAsRead(targetRooms: Seq[RoomId]) {
  implicit val context = AsyncEntityIOContextOnV0Api.fromThinConfig

  val logger = Logger.get(getClass)

  val messageRepo = AsyncMessageRepository.ofV0Api()

  val roomRepo = AsyncRoomRepository.ofV0Api()

  val handler = new AsyncAutoMarkAsRead(targetRooms)

  def start(): Unit = {
    val rooms: Future[List[Room]] = roomRepo.rooms()

    rooms.map {
      roomList =>
        roomList.foreach {
          room =>
//            room.attributes match {
//              case Some(a) if a.unreadCount > 0 =>
                if (targetRooms.contains(room.identity)) {
                  roomRepo.latestMessage(room.identity) map {
                    message =>
                      messageRepo.markAsRead(message)
                      logger.info(s"Mark room ${room.name}(${room.identity.value}) as read")
                  }
                }
//            }
        }
    }

    rooms.onSuccess {
      case r =>
        AsyncMessageEventPublisher.subscribe(handler)
    }
  }
}
