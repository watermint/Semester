package etude.vino.chatwork.ui.state

import etude.pintxos.chatwork.domain.model.room.{Room, RoomId}

import scala.collection.mutable

class Rooms {

}

object Rooms {
  val rooms = new mutable.HashMap[RoomId, Room]()

  val avatar = new Avatar[RoomId]()
}