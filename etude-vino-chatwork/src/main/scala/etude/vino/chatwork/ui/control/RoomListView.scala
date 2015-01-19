package etude.vino.chatwork.ui.control

import etude.pintxos.chatwork.domain.model.room.Room
import etude.vino.chatwork.domain.state.Rooms

import scalafx.scene.control.ListCell

class RoomListView extends EntityListView[Room] {
  def listCellForEntity() = {
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
}
