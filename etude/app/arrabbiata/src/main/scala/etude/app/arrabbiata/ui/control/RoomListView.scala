package etude.app.arrabbiata.ui.control

import java.util.concurrent.atomic.AtomicReference
import javafx.beans.value.{ObservableValue, ChangeListener}
import javafx.scene.control.{ListCell => JListCell, ListView => JListView}
import javafx.util.Callback

import etude.messaging.chatwork.domain.model.room.Room

import scalafx.scene.control.ListView

class RoomListView extends ListView[Room] {
  val selected = new AtomicReference[Room]()

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

  selectionModel().selectedItemProperty().addListener(
    new ChangeListener[Room] {
      override def changed(observable: ObservableValue[_ <: Room], oldValue: Room, newValue: Room): Unit = {
        selected.set(newValue)
        onSelected(newValue)
      }
    }
  )

  delegate.setCellFactory(new RoomCallback())

  def onSelected(room: Room): Unit = {}
}
