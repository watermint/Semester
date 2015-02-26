package semester.application.vino.service.markasread

import akka.actor.{Actor, ActorRef, Props}
import semester.application.vino.domain.Models
import semester.application.vino.service.api.{ApiEnqueue, PriorityP1}
import semester.service.chatwork.domain.service.request.ReadRequest
import semester.service.chatwork.domain.service.response.LoadChatResponse

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
          PriorityP1
        )
      }
  }
}

object AutoMarkAsRead {
  def props(apiHub: ActorRef): Props = Props(AutoMarkAsRead(apiHub))
}