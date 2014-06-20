package etude.app.arrabbiata.ui.message.composite.room

import etude.app.arrabbiata.ui.message.composite.CompositeUIMessage
import etude.app.arrabbiata.ui.pane.room.MergeRoomPane

case class SelectMergeRooms() extends CompositeUIMessage {
  override def perform(): Unit = {
    (MergeRoomPane.baseRoomList.delegate.getSelectionModel.getSelectedItem,
      MergeRoomPane.targetRoomList.delegate.getSelectionModel.getSelectedItem) match {
      case (null, _) =>
      case (_, null) =>
      case (base, target) =>
        MergeRoomPane.mergeButton.disable = false
    }
  }
}
