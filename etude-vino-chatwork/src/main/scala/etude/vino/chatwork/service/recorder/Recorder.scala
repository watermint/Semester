package etude.vino.chatwork.service.recorder

import akka.actor.{Actor, ActorRef, Props}
import etude.pintxos.chatwork.domain.service.v0.request.LoadChatRequest
import etude.pintxos.chatwork.domain.service.v0.response.{GetUpdateResponse, InitLoadResponse, LoadChatResponse, LoadOldChatResponse}
import etude.vino.chatwork.domain.lifecycle._
import etude.vino.chatwork.service.api.{ApiEnqueue, PriorityP2}

case class Recorder(apiHub: ActorRef) extends Actor {

  def receive: Receive = {
    case r: InitLoadResponse =>
      r.contacts.foreach { c => AccountRepository.update(c)}
      r.participants.foreach { p => ParticipantRepository.update(p)}
      r.rooms.foreach { r => RoomRepository.update(r)}
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
      r.chatList.foreach(MessageRepository.update)

    case r: LoadOldChatResponse =>
      r.messages.foreach(MessageRepository.update)
  }

}

object Recorder {
  def props(apiHub: ActorRef): Props = Props(Recorder(apiHub))
}