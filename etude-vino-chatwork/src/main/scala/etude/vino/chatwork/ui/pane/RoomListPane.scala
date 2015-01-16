package etude.vino.chatwork.ui.pane

import etude.pintxos.chatwork.domain.model.room.Room
import etude.vino.chatwork.ui.UIMessage
import etude.vino.chatwork.ui.control.RoomListView

import scalafx.collections.ObservableBuffer


object RoomListPane {
  val roomListView = new RoomListView()

  case class RoomListUpdate(rooms: Seq[Room]) extends UIMessage {
    def perform(): Unit = {
      roomListView.items = ObservableBuffer(rooms)
    }
  }
}
