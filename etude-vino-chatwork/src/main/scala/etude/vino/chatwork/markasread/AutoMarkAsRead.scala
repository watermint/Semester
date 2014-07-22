package etude.vino.chatwork.markasread

import java.nio.file.{Files, Paths}
import java.util.concurrent.{ExecutorService, Executors, TimeUnit}

import etude.epice.logging.LoggerFactory
import etude.pintxos.chatwork.domain.event.message.AsyncMessageEventPublisher
import etude.pintxos.chatwork.domain.infrastructure.api.AsyncEntityIOContextOnV0Api
import etude.pintxos.chatwork.domain.lifecycle.message.AsyncMessageRepository
import etude.pintxos.chatwork.domain.lifecycle.room.AsyncRoomRepository
import etude.pintxos.chatwork.domain.model.room.{Room, RoomId}

import scala.concurrent.{ExecutionContext, Future}
import scala.io.Source

case class AutoMarkAsRead(targetRooms: Seq[RoomId]) {
  val executorsPool: ExecutorService = Executors.newFixedThreadPool(10)

  implicit val executors = ExecutionContext.fromExecutorService(executorsPool)

  implicit val context = AsyncEntityIOContextOnV0Api.fromThinConfig

  val logger = LoggerFactory.getLogger(getClass)

  val messageRepo = AsyncMessageRepository.ofContext(context)

  val roomRepo = AsyncRoomRepository.ofContext(context)

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

object AutoMarkAsRead {
  def main(args: Array[String]) {
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