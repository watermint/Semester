package etude.vino.chatwork.ui.control

import etude.pintxos.chatwork.domain.model.room.Room
import etude.vino.chatwork.ui.state.Rooms

import scalafx.scene.control.{ListView, ListCell}

class RoomListView extends ListView[Room] {
  def listCell() = {
    new ListCell[Room] {
      item.onChange {
        (_, _, room) =>
          room match {
            case null =>
            case r =>
              text = r.name
              graphic = Rooms.avatar.nodeOf(r.roomId)
          }
      }
    }
  }

  cellFactory = {
    _ =>
      listCell()
  }
}
