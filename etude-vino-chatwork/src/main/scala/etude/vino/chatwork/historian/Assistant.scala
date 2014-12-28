package etude.vino.chatwork.historian

import java.time.{Duration, Instant}

import akka.actor.{Actor, Props}
import etude.epice.logging.LoggerFactory
import etude.pintxos.chatwork.domain.infrastructure.api.v0.request.{LoadChatRequest, LoadOldChatRequest}
import etude.pintxos.chatwork.domain.model.message.MessageId
import etude.pintxos.chatwork.domain.model.room.{Room, RoomId}
import etude.vino.chatwork.api.{ApiHub, PriorityLow}
import etude.vino.chatwork.historian.model.{Chunk, RoomChunk}
import etude.vino.chatwork.historian.operation.Traverse

case class Assistant(apiHub: ApiHub) extends Actor {
  def receive: Receive = {
    case t: Traverse =>
      traverse(t.room)
  }

  val logger = LoggerFactory.getLogger(getClass)

  val latestTimeGapInSeconds = 86400

  def traverse(room: Room): Unit = {
    logger.info(s"traverse: ${room.roomId} - ${room.description}")
    Historian.load(room.roomId) match {
      case None =>
        apiHub.enqueue(LoadChatRequest(room.roomId))(PriorityLow)
      case Some(json) =>
        traverse(RoomChunk.fromJSON(json))
    }
  }

  def traverse(roomChunk: RoomChunk): Unit = {
    val roomId = RoomId(roomChunk.roomId)
    if (roomChunk.chunks.maxBy(_.highTime).highTime.isBefore(Instant.now.minusSeconds(latestTimeGapInSeconds))) {
      apiHub.enqueue(LoadChatRequest(roomId))(PriorityLow)
    } else {
      Chunk.nextChunkMessageId(roomChunk.chunks, Instant.now.minus(Duration.ofDays(30))) match {
        case None =>
        // NOP
        case Some(msgId) =>
          apiHub.enqueue(LoadOldChatRequest(MessageId(roomId, msgId)))(PriorityLow)
      }
    }
  }
}

object Assistant {
  def props(apiHub: ApiHub): Props = Props(Assistant(apiHub))
}