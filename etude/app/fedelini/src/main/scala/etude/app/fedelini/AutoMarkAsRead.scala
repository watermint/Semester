package etude.app.fedelini

import scala.concurrent.ExecutionContext.Implicits.global
import etude.messaging.chatwork.domain.model.room.{Room, RoomId}
import etude.messaging.chatwork.domain.infrastructure.api.AsyncEntityIOContextOnV0Api
import etude.messaging.chatwork.domain.lifecycle.room.AsyncRoomRepository
import etude.app.fedelini.domain.AsyncAutoMarkAsRead
import etude.messaging.chatwork.domain.lifecycle.message.AsyncMessageRepository
import scala.concurrent.Future
import grizzled.slf4j.Logger
import etude.messaging.chatwork.domain.event.message.AsyncMessageEventPublisher

case class AutoMarkAsRead(targetRooms: Seq[RoomId]) {
  implicit val context = AsyncEntityIOContextOnV0Api.fromThinConfig

  val logger = Logger[this.type]

  val messageRepo = AsyncMessageRepository.ofV0Api()

  val roomRepo = AsyncRoomRepository.ofV0Api()

  val handler = new AsyncAutoMarkAsRead(targetRooms)

  def start(): Unit = {
    val rooms: Future[List[Room]] = roomRepo.rooms()

    rooms.map {
      roomList =>
        roomList.foreach {
          room =>
            if (targetRooms.contains(room.identity)) {
              roomRepo.latestMessage(room.identity) map {
                message =>
                  messageRepo.markAsRead(message)
                  logger.info(s"Mark room ${room.name}(${room.identity.value}) as read")
              }
            }
        }
    }

    rooms.onSuccess {
      case r =>
        AsyncMessageEventPublisher.subscribe(handler)
    }
  }
}
