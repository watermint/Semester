package etude.vino.chatwork.service.recorder

import akka.actor.{Actor, ActorRef, Props}
import etude.pintxos.chatwork.domain.service.v0.request.LoadChatRequest
import etude.pintxos.chatwork.domain.service.v0.response.{GetUpdateResponse, InitLoadResponse, LoadChatResponse, LoadOldChatResponse}
import etude.vino.chatwork.model.storage._
import etude.vino.chatwork.service.api.{ApiEnqueue, PriorityP2}

case class Recorder(apiHub: ActorRef) extends Actor {

  def receive: Receive = {
    case r: InitLoadResponse =>
      r.contacts.foreach { c => AccountStorage.store(c)}
      r.participants.foreach { p => ParticipantStorage.store(p)}
      r.rooms.foreach { r => RoomStorage.store(r)}
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
      r.chatList.foreach(MessageStorage.store)

    case r: LoadOldChatResponse =>
      r.messages.foreach(MessageStorage.store)
  }

}

object Recorder {
  def props(apiHub: ActorRef): Props = Props(Recorder(apiHub))
}