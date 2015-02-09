package etude.vino.chatwork.ui.pane

import java.time.{Duration, Instant}

import etude.epice.logging.LoggerFactory
import etude.pintxos.chatwork.domain.model.message.Message
import etude.pintxos.chatwork.domain.model.room.{Room, RoomId}
import etude.vino.chatwork.domain.Models
import etude.vino.chatwork.domain.lifecycle.SearchOptions
import etude.vino.chatwork.domain.model.MarkAsRead
import etude.vino.chatwork.domain.state.Rooms
import etude.vino.chatwork.ui.control.{MessageListView, RoomListView}
import etude.vino.chatwork.ui.{UI, UILogic, UIMessage, UIStyles}
import org.elasticsearch.index.query.{QueryBuilder, QueryBuilders}
import org.elasticsearch.search.aggregations.AggregationBuilders
import org.elasticsearch.search.aggregations.bucket.terms.Terms
import org.elasticsearch.search.sort.{SortBuilders, SortOrder}

import scalafx.Includes._
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.{CheckBox, Label, SplitPane}
import scalafx.scene.layout.{BorderPane, FlowPane}

object ChatRoomsPane {
  val logger = LoggerFactory.getLogger(getClass)

  val roomListView = new RoomListView() {
    onMouseClicked = handle {
      val room = delegate.getSelectionModel.getSelectedItem
      UI.ref ! RoomUIUpdate(room)
    }
  }

  def searchStartDate(): Instant = {
    Instant.now().minus(Duration.ofDays(14))
  }

  def queryForRooms(): QueryBuilder = {
    QueryBuilders.boolQuery().must(
      QueryBuilders.rangeQuery("@timestamp").gte(searchStartDate().toString)
    )
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
      Models.markAsReadRepository.get(room.roomId) match {
        case Some(m) =>
          UI.ref ! UpdateRoomMuteCheck(room, m.markAsRead)
        case None =>
          UI.ref ! UpdateRoomMuteCheck(room, false)
      }

      UpdateTimelineForRoom(messages.entities)
    }
  }

  private case class UpdateRoomMuteCheck(room: Room, selected: Boolean) extends UIMessage {
    def perform(): Unit = {
      muteCheck.selected = selected
    }
  }

  private case class RoomListUpdateFromList(rooms: Seq[Room]) extends UIMessage {
    def perform(): Unit = {
      roomListView.items = ObservableBuffer(rooms)
    }
  }

  case class RoomListUpdate(rooms: Seq[Room]) extends UILogic {
    def perform(): UIMessage = {
      val result = Models.messageRepository.search(
        query = queryForRooms(),
        options = SearchOptions(
          aggregations = Some(
            AggregationBuilders.terms("aggs_room").field("room").size(100)
          )
        )
      )

      val filteredRooms: Seq[Room] = result.aggregations.get("aggs_room") match {
        case Some(terms: Terms) =>
          Range.inclusive(0, terms.getBuckets.size() - 1).flatMap {
            i =>
              val bucket = terms.getBuckets.get(i)
              val roomId = RoomId(BigInt(bucket.getKey))
              Rooms.room(roomId)
          }

        case _ =>
          Seq()
      }

      RoomListUpdateFromList(filteredRooms.sortBy(_.name))
    }
  }

  case class UpdateTimelineForRoom(messages: Seq[Message]) extends UIMessage {
    def perform(): Unit = {
      messagePane.center = new MessageListView {
        items = ObservableBuffer(messages)
      }
    }
  }

  case class MuteRoom(room: Room, muteEnabled: Boolean) extends UILogic {
    def perform(): UIMessage = {
      if (muteEnabled) {
        Models.markAsReadRepository.update(MarkAsRead(room.roomId, markAsRead = true))
      } else {
        Models.markAsReadRepository.delete(MarkAsRead(room.roomId, markAsRead = false))
      }

      // nop
      new UIMessage() {
        def perform(): Unit = {}
      }
    }
  }

  val muteCheck = new CheckBox {
    text = "Mute"
    onAction = handle {
      UI.ref ! MuteRoom(roomListView.getSelectionModel.getSelectedItem, this.selected.value)
    }
  }

  val messagePane = new BorderPane {
    top = new FlowPane {
      padding = UIStyles.paddingInsets
      children = Seq(
        muteCheck
      )
    }
    center = new MessageListView()
  }

  val chatRoomCenterPane = new SplitPane {
    padding = UIStyles.paddingInsets
    dividerPositions_=(0.2, 0.8)
    items ++= Seq(roomListView, messagePane)
  }

  val chatRoomPane = new BorderPane {
    padding = UIStyles.paddingInsets
    top = new FlowPane {
      padding = UIStyles.paddingInsets
      children = Seq(
        new Label("Room")
      )
    }
    center = chatRoomCenterPane
  }

}
