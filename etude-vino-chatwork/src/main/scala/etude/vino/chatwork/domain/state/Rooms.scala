package etude.vino.chatwork.domain.state

import akka.actor.{Actor, Props}
import etude.pintxos.chatwork.domain.model.room.{Room, RoomId}
import etude.pintxos.chatwork.domain.service.v0.response.InitLoadResponse
import etude.vino.chatwork.ui.Main.ApplicationReady
import etude.vino.chatwork.ui.UI
import etude.vino.chatwork.ui.pane.ChatRoomsPane.RoomListUpdate

import scala.collection.mutable

class Rooms extends Actor {
  def publishToUI(): Unit = {
    UI.ref ! RoomListUpdate(Rooms.rooms.values.toSeq)
  }

  def receive: Receive = {
    case r: InitLoadResponse =>
      r.rooms.foreach {
        room =>
          self ! room
      }
      publishToUI()

    case r: Room =>
      Rooms.avatar.updateAvatar(r.roomId, r.avatar)
      Rooms.rooms.put(r.roomId, r)
  }
}

object Rooms {
  val rooms = new mutable.HashMap[RoomId, Room]()

  val avatar = new Avatar[RoomId]()

  val actorRef = UI.system.actorOf(Props[Rooms])

  def room(roomId: RoomId): Option[Room] = {
    rooms.get(roomId)
  }
}