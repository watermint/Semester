package semester.application.vino.domain.state

import akka.actor.{Actor, Props}
import semester.service.chatwork.domain.model.room.{Room, RoomId}
import semester.service.chatwork.domain.service.v0.response.{GetUpdateResponse, InitLoadResponse}
import semester.application.vino.domain.Models
import semester.application.vino.ui.UI
import semester.application.vino.ui.pane.ChatRoomsPane.RoomListUpdate

import scala.collection.mutable

class Rooms extends Actor {
  def publishToUI(): Unit = {
    UI.ref ! RoomListUpdate()
  }

  def receive: Receive = {
    case r: InitLoadResponse =>
      r.rooms.foreach {
        room =>
          self ! room
      }

    case _: GetUpdateResponse =>
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
    rooms.get(roomId) match {
      case Some(r) => Some(r)
      case None =>
        Models.roomRepository.get(roomId) match {
          case Some(room) =>
            rooms.put(room.roomId, room)
            Some(room)
          case _ =>
            None
        }
    }
  }
}