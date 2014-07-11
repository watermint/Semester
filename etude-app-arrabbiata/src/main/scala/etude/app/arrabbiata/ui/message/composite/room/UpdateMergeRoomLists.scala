package etude.app.arrabbiata.ui.message.composite.room

import etude.app.arrabbiata.state.Rooms
import etude.app.arrabbiata.ui.message.composite.CompositeUIMessage
import etude.app.arrabbiata.ui.pane.room.MergeRoomPane
import etude.adapter.chatwork.domain.model.room.{Room, RoomTypeGroup}

import scalafx.scene.control.TextField

case class UpdateMergeRoomLists() extends CompositeUIMessage {
  def displayRooms(rooms: Seq[Room]): Seq[Room] = {
    sortRooms(filterRooms(rooms))
  }

  def sortRooms(rooms: Seq[Room]): Seq[Room] = {
    rooms.sortBy(_.name)
  }

  def filterRooms(rooms: Seq[Room]): Seq[Room] = {
    rooms.filter {
      r =>
        r.roomType match {
          case _: RoomTypeGroup => true
          case _ => false
        }
    }
  }
  
  def search(text: String, rooms: Seq[Room]): Seq[Room] = {
    text match {
      case null => displayRooms(rooms)
      case s if text.trim.isEmpty => displayRooms(rooms)
      case s => displayRooms(rooms.filter(_.name.toLowerCase.contains(text.toLowerCase)))
    }
  }

  def search(text: TextField, rooms: Seq[Room]): Seq[Room] = search(text.text.value, rooms)

  def perform(): Unit = {
    MergeRoomPane.diffParticipantItems.clear()
    MergeRoomPane.mergeButton.disable = true

    Rooms.rooms.get() match {
      case null =>
        MergeRoomPane.targetRoomItems.clear()
        MergeRoomPane.baseRoomItems.clear()

        MergeRoomPane.searchRoomTarget.disable = true
        MergeRoomPane.searchRoomBase.disable = true

      case rooms =>
        MergeRoomPane.targetRoomItems.clear()
        MergeRoomPane.targetRoomItems.appendAll(search(MergeRoomPane.searchRoomTarget, rooms))
        MergeRoomPane.baseRoomItems.clear()
        MergeRoomPane.baseRoomItems.appendAll(search(MergeRoomPane.searchRoomBase, rooms))

        MergeRoomPane.searchRoomTarget.disable = false
        MergeRoomPane.searchRoomBase.disable = false
    }
  }
}
