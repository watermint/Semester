package etude.vino.chatwork.markasread

import akka.actor.{Actor, ActorRef, Props}
import etude.pintxos.chatwork.domain.infrastructure.api.v0.request.ReadRequest
import etude.pintxos.chatwork.domain.infrastructure.api.v0.response.LoadChatResponse
import etude.pintxos.chatwork.domain.model.room.RoomId
import etude.vino.chatwork.api.{ApiEnqueue, PriorityNormal}
import etude.vino.chatwork.storage.Storage
import org.json4s.JsonDSL._

case class MarkAsRead(apiHub: ActorRef)
  extends Actor {

  def receive: Receive = {
    case r: LoadChatResponse =>
      val lastMessage = r.chatList.maxBy(_.messageId.messageId).messageId
      if (MarkAsRead.load(lastMessage.roomId)) {
        apiHub ! ApiEnqueue(
          ReadRequest(
            lastMessage.roomId,
            lastMessage
          ),
          PriorityNormal
        )
      }
  }
}

object MarkAsRead {
  val indexName = "cw-markasread"

  val typeName = "markasread"

  def props(apiHub: ActorRef): Props = Props(MarkAsRead(apiHub))

  def load(roomId: RoomId): Boolean = {
    Storage.load(indexName, typeName, roomId.value.toString()) match {
      case None =>
        false

      case Some(json) =>
        true
    }
  }

  def store(roomId: RoomId, markAsRead: Boolean): Unit = {
    if (markAsRead) {
      val value = ("roomId" -> roomId.value) ~
        ("markasread" -> true)

      Storage.store(indexName, typeName, roomId.value.toString(), value)
    } else {
      Storage.delete(indexName, typeName, roomId.value.toString())
    }
  }
}