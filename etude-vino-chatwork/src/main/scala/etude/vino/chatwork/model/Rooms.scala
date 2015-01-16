package etude.vino.chatwork.model

import akka.actor.{Props, Actor}
import etude.pintxos.chatwork.domain.model.room.{Room, RoomId}
import etude.pintxos.chatwork.domain.service.v0.response.{LoadChatResponse, InitLoadResponse}
import etude.vino.chatwork.ui.UI
import etude.vino.chatwork.ui.pane.{MessageListPane, RoomListPane}

import scala.collection.mutable

class Rooms extends Actor {
  def receive: Receive = {
    case r: InitLoadResponse =>
      r.rooms.foreach {
        room =>
          self ! room
      }

    case r: Room =>
      Rooms.avatar.updateAvatar(r.roomId, r.avatar)
      Rooms.rooms.put(r.roomId, r)
  }
}

object Rooms {
  val rooms = new mutable.HashMap[RoomId, Room]()

  val avatar = new Avatar[RoomId]()

  val actorRef = UI.system.actorOf(Props[Rooms])
}