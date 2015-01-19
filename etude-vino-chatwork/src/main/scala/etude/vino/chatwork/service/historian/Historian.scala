package etude.vino.chatwork.service.historian

import java.time.Instant

import akka.actor.{Actor, ActorRef, Props}
import etude.epice.logging.LoggerFactory
import etude.pintxos.chatwork.domain.model.room._
import etude.pintxos.chatwork.domain.service.v0.request.LoadOldChatRequest
import etude.pintxos.chatwork.domain.service.v0.response.{InitLoadResponse, LoadChatResponse, LoadOldChatResponse}
import etude.vino.chatwork.domain.Models
import etude.vino.chatwork.domain.model.{RoomChunkId, Chunk, RoomChunk}
import etude.vino.chatwork.service.api.{Api, ApiEnqueue, PriorityP3, PriorityP4}

case class Historian(apiHub: ActorRef)
  extends Actor {

  val logger = LoggerFactory.getLogger(getClass)

  val traverse = Api.system.actorOf(Traverse.props(apiHub))

  val priorityLoadingDurationInSeconds = 86400 * 2

  def receive: Receive = {
    case r: InitLoadResponse =>
      val touches = touchTimes(r.rooms)
      touches
        .filter(_.room.roomType.equals(RoomTypeGroup()))
        .sortBy(_.touchTime)
        .foreach {
        t =>
          traverse ! TraverseRoom(t.room)
      }
      touches
        .filter(_.room.roomType.equals(RoomTypeDirect()))
        .sortBy(_.touchTime)
        .foreach {
        t =>
          traverse ! TraverseRoom(t.room)
      }

    case r: LoadChatResponse =>
      if (r.chatList.size < 3) {
        logger.debug(s"Loading chat for room ${r.chatList.last.messageId.roomId} reached EPOCH.")
        updateChunk(r.chatList.last.messageId.roomId, Chunk.epochChunk(r.chatList.last.messageId))
      } else {
        updateChunk(
          r.chatList.last.messageId.roomId,
          Chunk.fromMessages(r.chatList)
        )
        traverse ! NextChunk(r.chatList.seq.minBy(_.messageId.messageId).messageId)
      }

    case r: LoadOldChatResponse =>
      if (r.messages.size == 0) {
        logger.debug(s"Loading chat for room ${r.lastMessage.roomId} reached EPOCH.")
        updateChunk(r.lastMessage.roomId, Chunk.epochChunk(r.lastMessage))
      } else {
        updateChunk(r.lastMessage.roomId, Chunk.fromMessages(r.messages))
        val lwm = r.messages.minBy(_.messageId.messageId)
        val priority = if (lwm.ctime.isBefore(Instant.now.minusSeconds(priorityLoadingDurationInSeconds))) {
          PriorityP4
        } else {
          PriorityP3
        }
        apiHub ! ApiEnqueue(LoadOldChatRequest(lwm.messageId), priority)
      }
  }

  case class TouchTime(room: Room, touchTime: Instant)

  def touchTimes(rooms: Seq[Room]): Seq[TouchTime] = {
    rooms.map {
      room =>
        Models.roomChunkRepository.get(room.roomId) match {
          case Some(chunk) =>
            TouchTime(room, chunk.chunks.maxBy(_.touchTime).touchTime)
          case None =>
            TouchTime(room, Instant.EPOCH)
        }
    }
  }

  def updateChunk(roomId: RoomId, chunk: Chunk): Unit = {
    Models.roomChunkRepository.get(roomId) match {
      case None =>
        val roomChunk = RoomChunk(
          RoomId(roomId.value),
          Seq(chunk)
        )
        Models.roomChunkRepository.update(roomChunk)

      case Some(roomChunk) =>
        val updatedRoomChunk = RoomChunk(
          RoomId(roomId.value),
          Chunk.compaction(roomChunk.chunks :+ chunk)
        )
        Models.roomChunkRepository.update(updatedRoomChunk)
    }
  }

}

object Historian {
  def props(apiHub: ActorRef): Props = Props(Historian(apiHub))
}