package etude.vino.chatwork.historian

import java.time.{Duration, Instant}

import etude.epice.logging.LoggerFactory
import etude.pintxos.chatwork.domain.infrastructure.api.v0.request.{LoadChatRequest, LoadOldChatRequest}
import etude.pintxos.chatwork.domain.infrastructure.api.v0.response.{ChatWorkResponse, InitLoadResponse, LoadChatResponse, LoadOldChatResponse}
import etude.pintxos.chatwork.domain.model.message.MessageId
import etude.pintxos.chatwork.domain.model.room.{Room, RoomId}
import etude.vino.chatwork.api.{ApiHub, ApiSubscriber, PriorityLow}
import etude.vino.chatwork.historian.model.{Chunk, RoomChunk}
import etude.vino.chatwork.storage.Storage

import scala.util.Random

case class Historian(apiHub: ApiHub)
  extends ApiSubscriber {

  apiHub.addSubscriber(this)

  val logger = LoggerFactory.getLogger(getClass)

  val indexName = "cw-historian-room"
  val typeName = "room-chunk"

  val latestTimeGapInSeconds = 86400

  def receive: PartialFunction[ChatWorkResponse, Unit] = {
    case r: InitLoadResponse =>
      Random.shuffle(r.rooms).foreach(traverse)

    case r: LoadChatResponse =>
      updateChunk(
        r.chatList.last.messageId.roomId,
        Chunk.fromMessages(r.chatList)
      )

    case r: LoadOldChatResponse =>
      if (r.messages.size == 0) {
        updateChunk(r.lastMessage.roomId, Chunk.epochChunk(r.lastMessage))
      } else {
        updateChunk(r.lastMessage.roomId, Chunk.fromMessages(r.messages))
        apiHub.enqueue(LoadOldChatRequest(r.messages.minBy(_.messageId.messageId).messageId))(PriorityLow)
      }
  }

  def updateChunk(roomId: RoomId, chunk: Chunk): Unit = {
    Storage.load(indexName, typeName, roomId.value.toString()) match {
      case None =>
        val roomChunk = RoomChunk(
          roomId.value,
          Seq(chunk)
        )
        Storage.store(indexName, typeName, roomId.value.toString(), roomChunk.toJSON)

      case Some(json) =>
        val roomChunk = RoomChunk.fromJSON(json)
        val updatedRoomChunk = RoomChunk(
          roomId.value,
          Chunk.compaction(roomChunk.chunks :+ chunk)
        )
        Storage.store(indexName, typeName, roomId.value.toString(), updatedRoomChunk.toJSON)
    }
  }

  def traverse(room: Room): Unit = {
    logger.info(s"traverse: ${room.roomId} - ${room.description}")
    Storage.load(indexName, typeName, room.roomId.value.toString()) match {
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
