package etude.vino.chatwork.historian

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import etude.epice.logging.LoggerFactory
import etude.pintxos.chatwork.domain.infrastructure.api.v0.request.LoadOldChatRequest
import etude.pintxos.chatwork.domain.infrastructure.api.v0.response.{InitLoadResponse, LoadChatResponse, LoadOldChatResponse}
import etude.pintxos.chatwork.domain.model.room.RoomId
import etude.vino.chatwork.api.{ApiEnqueue, PriorityLow}
import etude.vino.chatwork.historian.model.{Chunk, RoomChunk}
import etude.vino.chatwork.historian.operation.{NextChunk, Traverse}
import etude.vino.chatwork.storage.Storage

import scala.util.Random

case class Historian(apiHub: ActorRef)
  extends Actor {

  val logger = LoggerFactory.getLogger(getClass)

  val assistant = Historian.system.actorOf(Assistant.props(apiHub))

  def receive: Receive = {
    case r: InitLoadResponse =>
      Random.shuffle(r.rooms).foreach {
        r =>
          assistant ! Traverse(r)
      }

    case r: LoadChatResponse =>
      updateChunk(
        r.chatList.last.messageId.roomId,
        Chunk.fromMessages(r.chatList)
      )
      assistant ! NextChunk(r.chatList.seq.minBy(_.messageId.messageId).messageId)

    case r: LoadOldChatResponse =>
      if (r.messages.size == 0) {
        logger.info(s"Loading chat for room ${r.lastMessage.roomId} reached EPOCH.")
        updateChunk(r.lastMessage.roomId, Chunk.epochChunk(r.lastMessage))
      } else {
        updateChunk(r.lastMessage.roomId, Chunk.fromMessages(r.messages))
        apiHub ! ApiEnqueue(LoadOldChatRequest(r.messages.minBy(_.messageId.messageId).messageId), PriorityLow)
      }
  }

  def updateChunk(roomId: RoomId, chunk: Chunk): Unit = {
    Historian.load(roomId) match {
      case None =>
        val roomChunk = RoomChunk(
          roomId.value,
          Seq(chunk)
        )
        Historian.store(roomId, roomChunk)

      case Some(roomChunk) =>
        val updatedRoomChunk = RoomChunk(
          roomId.value,
          Chunk.compaction(roomChunk.chunks :+ chunk)
        )
        Historian.store(roomId, updatedRoomChunk)
    }
  }

}

object Historian {

  def load(roomId: RoomId): Option[RoomChunk] = {
    Storage.load(indexName, typeName, roomId.value.toString()) match {
      case None =>
        None
      case Some(json) =>
        Some(RoomChunk.fromJSON(json))
    }
  }

  def store(roomId: RoomId, chunk: RoomChunk): Long = {
    Storage.store(indexName, typeName, roomId.value.toString(), chunk.toJSON)
  }

  val indexName = "cw-historian-room"

  val typeName = "room-chunk"

  def props(apiHub: ActorRef): Props = Props(Historian(apiHub))

  val system = ActorSystem("cw-historian")
}