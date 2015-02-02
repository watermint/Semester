package etude.vino.chatwork.ui.pane

import etude.pintxos.chatwork.domain.model.room.Room
import etude.vino.chatwork.domain.Models
import etude.vino.chatwork.domain.lifecycle.SearchOptions
import etude.vino.chatwork.ui.control.RoomListView
import etude.vino.chatwork.ui.pane.MessageListPane.UpdateTimelineForRoom
import etude.vino.chatwork.ui.{UI, UILogic, UIMessage}
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.sort.{SortBuilders, SortOrder}

import scalafx.Includes._
import scalafx.collections.ObservableBuffer


object RoomListPane {
  val roomListView = new RoomListView() {
    onMouseClicked = handle {
      val room = delegate.getSelectionModel.getSelectedItem
      UI.ref ! RoomUIUpdate(room)
    }
  }

  case class RoomUIUpdate(room: Room) extends UILogic {
    def perform(): UIMessage = {
      val messages = Models.messageRepository.search(
        query = QueryBuilders.matchQuery("room", room.roomId.value),
        options = SearchOptions(
          sort = Some(SortBuilders.fieldSort("@timestamp").order(SortOrder.DESC)),
          size = Some(50)
        )
      )
      UpdateTimelineForRoom(messages.entities)
    }
  }

  case class RoomListUpdate(rooms: Seq[Room]) extends UIMessage {
    def perform(): Unit = {
      roomListView.items = ObservableBuffer(rooms)
    }
  }

}
