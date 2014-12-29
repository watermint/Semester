package etude.vino.chatwork.historian

import java.time.{Duration, Instant}

import akka.actor.{Actor, ActorRef, Props}
import etude.epice.logging.LoggerFactory
import etude.pintxos.chatwork.domain.infrastructure.api.v0.request.{LoadChatRequest, LoadOldChatRequest}
import etude.pintxos.chatwork.domain.model.message.MessageId
import etude.pintxos.chatwork.domain.model.room.RoomId
import etude.vino.chatwork.api.{PriorityLower, ApiEnqueue, PriorityLow}
import etude.vino.chatwork.historian.model.{Chunk, RoomChunk}
import etude.vino.chatwork.historian.operation.{NextChunk, Traverse}

case class Assistant(apiHub: ActorRef) extends Actor {
  def receive: Receive = {
    case t: Traverse =>
      traverse(t)

    case n: NextChunk =>
      nextChunk(n)
  }

  val logger = LoggerFactory.getLogger(getClass)

  val latestTimeGapInSeconds = 600

  val nextChunkTerm =  Instant.now.minus(Duration.ofDays(365 * 2))

  def traverse(traverse: Traverse): Unit = {
    val room = traverse.room
    Historian.load(room.roomId) match {
      case None =>
        apiHub ! ApiEnqueue(LoadChatRequest(room.roomId), PriorityLower)
      case Some(chunk) =>
        traverseChunk(chunk)
    }
  }

  def traverseChunk(roomChunk: RoomChunk): Unit = {
    val roomId = RoomId(roomChunk.roomId)
    if (roomChunk.chunks.maxBy(_.touchTime).touchTime.isBefore(Instant.now.minusSeconds(latestTimeGapInSeconds))) {
      apiHub ! ApiEnqueue(LoadChatRequest(roomId), PriorityLow)
    } else {
      Chunk.nextChunkMessageId(roomChunk.chunks, nextChunkTerm) match {
        case None => // NOP
        case Some(msgId) =>
          apiHub ! ApiEnqueue(LoadOldChatRequest(MessageId(roomId, msgId)), PriorityLow)
      }
    }
  }

  def nextChunk(nextChunk: NextChunk): Unit = {
    val lastMessageId = nextChunk.lastMessageId
    Historian.load(lastMessageId.roomId) match {
      case None => // NOP
      case Some(chunk) =>
        Chunk.nextChunkMessageId(chunk.chunks, nextChunkTerm) match {
          case None => // NOP
          case Some(msgId) =>
            apiHub ! ApiEnqueue(LoadOldChatRequest(MessageId(lastMessageId.roomId, msgId)), PriorityLow)
        }
    }
  }
}

object Assistant {
  def props(apiHub: ActorRef): Props = Props(Assistant(apiHub))
}