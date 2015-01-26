package etude.vino.chatwork.service.historian

import java.time.{Duration, Instant}

import akka.actor.{Actor, ActorRef, Props}
import etude.epice.logging.LoggerFactory
import etude.pintxos.chatwork.domain.model.message.MessageId
import etude.pintxos.chatwork.domain.model.room._
import etude.pintxos.chatwork.domain.service.v0.request.{LoadChatRequest, LoadOldChatRequest}
import etude.pintxos.chatwork.domain.service.v0.response.{InitLoadResponse, LoadChatResponse, LoadOldChatResponse}
import etude.vino.chatwork.domain.Models
import etude.vino.chatwork.domain.model.{Chunk, RoomChunk}
import etude.vino.chatwork.service.api._

case class Historian(apiHub: ActorRef)
  extends Actor {

  val logger = LoggerFactory.getLogger(getClass)

  val priorityLoadingDurationInSeconds = 86400 * 2

  case class TouchTime(room: Room, touchTime: Instant)

  def receive: Receive = {
    case t: TraverseRoom =>
      traverse(t)

    case n: NextChunk =>
      nextChunk(n)

    case r: InitLoadResponse =>
      val touches = touchTimes(r.rooms)
      touches
        .filter(_.room.roomType.equals(RoomTypeGroup()))
        .sortBy(_.touchTime)
        .foreach {
        t =>
          self ! TraverseRoom(t.room)
      }
      touches
        .filter(_.room.roomType.equals(RoomTypeDirect()))
        .sortBy(_.touchTime)
        .foreach {
        t =>
          self ! TraverseRoom(t.room)
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
        self ! NextChunk(r.chatList.seq.minBy(_.messageId.messageId).messageId)
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

  val latestTimeGapInSeconds = 600

  val deferLoadingDurationInSeconds = 86400 * 7

  val nextChunkTerm =  Instant.now.minus(Duration.ofDays(365 * 2))

  def priorityOf(room: Room): Priority = {
    room.roomType match {
      case t if RoomType.isGroupRoom(t) => PriorityP4
      case _ => PriorityP5
    }
  }

  def traverse(traverse: TraverseRoom): Unit = {
    val room = traverse.room
    Models.roomChunkRepository.get(room.roomId) match {
      case None =>
        apiHub ! ApiEnqueue(LoadChatRequest(room.roomId), priorityOf(room))
      case Some(chunk) =>
        traverseChunk(chunk, room)
    }
  }

  def traverseChunk(roomChunk: RoomChunk, room: Room): Unit = {
    val roomId = RoomId(roomChunk.roomId.value)
    if (roomChunk.chunks.maxBy(_.touchTime).touchTime.isBefore(Instant.now.minusSeconds(latestTimeGapInSeconds))) {
      apiHub ! ApiEnqueue(LoadChatRequest(roomId), priorityOf(room))
    } else {
      Chunk.nextChunkMessageId(roomChunk.chunks, nextChunkTerm) match {
        case None => // NOP
        case Some(msgId) =>
          apiHub ! ApiEnqueue(LoadOldChatRequest(MessageId(roomId, msgId)), PriorityP3)
      }
    }
  }

  def nextChunk(nextChunk: NextChunk): Unit = {
    val lastMessageId = nextChunk.lastMessageId
    Models.roomChunkRepository.get(lastMessageId.roomId) match {
      case None => // NOP
      case Some(chunk) =>
        Chunk.nextChunkMessageId(chunk.chunks, nextChunkTerm) match {
          case None => // NOP
          case Some(msgId) =>
            apiHub ! ApiEnqueue(LoadOldChatRequest(MessageId(lastMessageId.roomId, msgId)), PriorityP3)
        }
    }
  }

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