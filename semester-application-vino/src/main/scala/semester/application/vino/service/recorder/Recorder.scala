package semester.application.vino.service.recorder

import akka.actor.{Actor, ActorRef, Props}
import semester.service.chatwork.domain.service.request.LoadChatRequest
import semester.service.chatwork.domain.service.response.{GetUpdateResponse, InitLoadResponse, LoadChatResponse, LoadOldChatResponse}
import semester.application.vino.domain.Models
import semester.application.vino.service.api.{ApiEnqueue, PriorityP2}

case class Recorder(apiHub: ActorRef) extends Actor {

  def receive: Receive = {
    case r: InitLoadResponse =>
      r.contacts.foreach { c => Models.accountRepository.update(c)}
      r.participants.foreach { p => Models.participantRepository.update(p)}
      r.rooms.foreach { r => Models.roomRepository.update(r)}
      r.rooms
        .filter(_.attributes.isDefined)
        .filter(_.attributes.get.unreadCount > 0)
        .foreach {
        room =>
          apiHub ! ApiEnqueue(LoadChatRequest(room.roomId), PriorityP2)
      }

    case r: GetUpdateResponse =>
      r.roomUpdateInfo foreach {
        room =>
          apiHub ! ApiEnqueue(LoadChatRequest(room.roomId), PriorityP2)
      }

    case r: LoadChatResponse =>
      r.chatList.foreach(Models.messageRepository.update)

    case r: LoadOldChatResponse =>
      r.messages.foreach(Models.messageRepository.update)
  }

}

object Recorder {
  def props(apiHub: ActorRef): Props = Props(Recorder(apiHub))
}