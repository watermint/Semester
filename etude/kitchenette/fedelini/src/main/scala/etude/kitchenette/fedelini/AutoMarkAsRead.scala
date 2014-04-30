package etude.kitchenette.fedelini

import etude.messaging.chatwork.domain.model.room.{Room, RoomId}
import etude.messaging.chatwork.domain.infrastructure.api.AsyncEntityIOContextOnV0Api
import etude.messaging.chatwork.domain.lifecycle.room.AsyncRoomRepository
import etude.messaging.chatwork.domain.lifecycle.message.AsyncMessageRepository
import scala.concurrent.{ExecutionContext, Future}
import etude.messaging.chatwork.domain.event.message.AsyncMessageEventPublisher
import java.nio.file.{Files, Paths}
import scala.io.Source
import java.util.concurrent.{TimeUnit, Executors, ExecutorService}
import etude.foundation.logging.LoggerFactory

case class AutoMarkAsRead(targetRooms: Seq[RoomId]) {
  val executorsPool: ExecutorService = Executors.newFixedThreadPool(10)

  implicit val executors = ExecutionContext.fromExecutorService(executorsPool)

  implicit val context = AsyncEntityIOContextOnV0Api.fromThinConfig

  val logger = LoggerFactory.getLogger(getClass)

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

object AutoMarkAsRead {
  def main (args: Array[String]) {
    val markAsReadList = Paths.get(System.getProperty("user.home"), ".etude-chatwork/markasread")
    if (Files.exists(markAsReadList)) {
      val list: Seq[RoomId] = Source.fromFile(markAsReadList.toFile).getLines().map {
        line =>
          RoomId(line.toInt)
      }.toSeq

      val auto = AutoMarkAsRead(list)

      auto.start()
      auto.executorsPool.awaitTermination(1, TimeUnit.MINUTES)

    } else {
      println(s"List file not found: $markAsReadList")
    }
  }
}