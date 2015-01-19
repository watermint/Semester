package etude.vino.chatwork.service.markasread

import akka.actor.{Actor, ActorRef, Props}
import etude.pintxos.chatwork.domain.service.v0.request.ReadRequest
import etude.pintxos.chatwork.domain.service.v0.response.LoadChatResponse
import etude.vino.chatwork.domain.Models
import etude.vino.chatwork.service.api.{ApiEnqueue, PriorityP2}

case class AutoMarkAsRead(apiHub: ActorRef)
  extends Actor {

  def receive: Receive = {
    case r: LoadChatResponse =>
      val lastMessage = r.chatList.maxBy(_.messageId.messageId).messageId
      if (Models.markAsReadRepository.get(lastMessage.roomId).isDefined) {
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

object AutoMarkAsRead {
  def props(apiHub: ActorRef): Props = Props(AutoMarkAsRead(apiHub))
}