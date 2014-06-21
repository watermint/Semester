package etude.app.arrabbiata.ui.control

import javafx.scene.control.{ListCell => JListCell, ListView => JListView}
import javafx.util.Callback

import etude.messaging.chatwork.domain.model.room.Room

class RoomListView extends DomainListView[Room] {
  class RoomListCell extends JListCell[Room] {
    override def updateItem(item: Room, empty: Boolean): Unit = {
      super.updateItem(item, empty)
      item match {
        case null =>
        case i =>
          setText(i.name)
      }
    }
  }

  class RoomCallback extends Callback[JListView[Room], JListCell[Room]] {
    override def call(param: JListView[Room]): JListCell[Room] = {
      new RoomListCell()
    }
  }

  delegate.setCellFactory(new RoomCallback())
}
