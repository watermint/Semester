package etude.vino.chatwork.historian

import akka.actor.{Actor, ActorSystem, Props}
import etude.epice.logging.LoggerFactory
import etude.pintxos.chatwork.domain.infrastructure.api.v0.request.LoadOldChatRequest
import etude.pintxos.chatwork.domain.infrastructure.api.v0.response.{InitLoadResponse, LoadChatResponse, LoadOldChatResponse}
import etude.pintxos.chatwork.domain.model.room.RoomId
import etude.vino.chatwork.api.{ApiHub, PriorityLow}
import etude.vino.chatwork.historian.model.{RoomChunk, Chunk}
import etude.vino.chatwork.historian.operation.Traverse
import etude.vino.chatwork.storage.Storage
import org.json4s.JValue

import scala.util.Random

case class Historian(apiHub: ApiHub)
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

    case r: LoadOldChatResponse =>
      if (r.messages.size == 0) {
        updateChunk(r.lastMessage.roomId, Chunk.epochChunk(r.lastMessage))
      } else {
        updateChunk(r.lastMessage.roomId, Chunk.fromMessages(r.messages))
        apiHub.enqueue(LoadOldChatRequest(r.messages.minBy(_.messageId.messageId).messageId))(PriorityLow)
      }
  }

  def updateChunk(roomId: RoomId, chunk: Chunk): Unit = {
    Historian.load(roomId) match {
      case None =>
        val roomChunk = RoomChunk(
          roomId.value,
          Seq(chunk)
        )
        Historian.store(roomId, roomChunk.toJSON)

      case Some(json) =>
        val roomChunk = RoomChunk.fromJSON(json)
        val updatedRoomChunk = RoomChunk(
          roomId.value,
          Chunk.compaction(roomChunk.chunks :+ chunk)
        )
        Historian.store(roomId, updatedRoomChunk.toJSON)
    }
  }

}

object Historian {

  def load(roomId: RoomId): Option[JValue] = {
    Storage.load(indexName, typeName, roomId.value.toString())
  }

  def store(roomId: RoomId, value: JValue): Long = {
    Storage.store(indexName, typeName, roomId.value.toString(), value)
  }

  val indexName = "cw-historian-room"

  val typeName = "room-chunk"

  def props(apiHub: ApiHub): Props = Props(Historian(apiHub))

  val system = ActorSystem("cw-historian")
}