package etude.app.arrabbiata.ui.message.composite.room

import etude.app.arrabbiata.controller.AppActor
import etude.app.arrabbiata.controller.message.room.MergeDiffList
import etude.app.arrabbiata.ui.message.composite.CompositeUIMessage
import etude.app.arrabbiata.ui.pane.room.MergeRoomPane

case class SelectMergeRooms() extends CompositeUIMessage {
  override def perform(): Unit = {
    (MergeRoomPane.baseRoomList.delegate.getSelectionModel.getSelectedItem,
      MergeRoomPane.targetRoomList.delegate.getSelectionModel.getSelectedItem) match {
      case (null, _) =>
        MergeRoomPane.mergeButton.disable = true
      case (_, null) =>
        MergeRoomPane.mergeButton.disable = true
      case (base, target) =>
        MergeRoomPane.mergeButton.disable = false
        AppActor.app ! MergeDiffList(base, target)
    }
  }
}
