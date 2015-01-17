package etude.vino.chatwork.service.markasread

import akka.actor.{Actor, ActorRef, Props}
import etude.pintxos.chatwork.domain.service.v0.request.ReadRequest
import etude.pintxos.chatwork.domain.service.v0.response.LoadChatResponse
import etude.pintxos.chatwork.domain.model.room.RoomId
import etude.vino.chatwork.domain.infrastructure.ElasticSearch
import etude.vino.chatwork.service.api.{ApiEnqueue, PriorityP2}
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
          PriorityP2
        )
      }
  }
}

object MarkAsRead {
  val indexName = "cw-markasread"

  val typeName = "markasread"

  def props(apiHub: ActorRef): Props = Props(MarkAsRead(apiHub))

  def load(roomId: RoomId): Boolean = {
    ElasticSearch.get(indexName, typeName, roomId.value.toString()) match {
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

      ElasticSearch.update(indexName, typeName, roomId.value.toString(), value)
    } else {
      ElasticSearch.delete(indexName, typeName, roomId.value.toString())
    }
  }
}