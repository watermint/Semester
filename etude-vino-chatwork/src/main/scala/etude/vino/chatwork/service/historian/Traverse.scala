package etude.vino.chatwork.service.historian

import java.time.{Duration, Instant}

import akka.actor.{Actor, ActorRef, Props}
import etude.epice.logging.LoggerFactory
import etude.pintxos.chatwork.domain.model.message.MessageId
import etude.pintxos.chatwork.domain.model.room.{Room, RoomId, RoomType}
import etude.pintxos.chatwork.domain.service.v0.request.{LoadChatRequest, LoadOldChatRequest}
import etude.vino.chatwork.domain.Models
import etude.vino.chatwork.domain.model.{Chunk, RoomChunk}
import etude.vino.chatwork.service.api._

case class Traverse(apiHub: ActorRef) extends Actor {
  def receive: Receive = {
    case t: TraverseRoom =>
      traverse(t)

    case n: NextChunk =>
      nextChunk(n)
  }

  val logger = LoggerFactory.getLogger(getClass)

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
}

object Traverse {
  def props(apiHub: ActorRef): Props = Props(Traverse(apiHub))
}