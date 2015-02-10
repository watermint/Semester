package etude.vino.chatwork.ui.pane

import java.util.concurrent.atomic.AtomicReference

import etude.epice.foundation.atomic.Reference
import etude.epice.logging.LoggerFactory
import etude.pintxos.chatwork.domain.model.message.Message
import etude.pintxos.chatwork.domain.model.room.{Room, RoomId}
import etude.vino.chatwork.domain.Models
import etude.vino.chatwork.domain.lifecycle.SearchOptions
import etude.vino.chatwork.domain.model.MarkAsRead
import etude.vino.chatwork.domain.state.Rooms
import etude.vino.chatwork.ui.control.{MessageListView, PeriodStartView, RoomListView}
import etude.vino.chatwork.ui.{UI, UILogic, UIMessage, UIStyles}
import org.elasticsearch.index.query.{QueryBuilder, QueryBuilders}
import org.elasticsearch.search.aggregations.AggregationBuilders
import org.elasticsearch.search.aggregations.bucket.terms.Terms
import org.elasticsearch.search.sort.{SortBuilders, SortOrder}

import scalafx.Includes._
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.{CheckBox, SplitPane, TextField}
import scalafx.scene.layout.{BorderPane, FlowPane, HBox}

object ChatRoomsPane {
  val logger = LoggerFactory.getLogger(ChatRoomsPane.getClass)

  class RoomListViewWithUIUpdate extends RoomListView {
    onMouseClicked = handle {
      val room = delegate.getSelectionModel.getSelectedItem
      UI.ref ! RoomUIUpdate(room)
    }
  }

  def startDateQuery(): Option[QueryBuilder] = {
    searchStartSelector.selectedTime map {
      case t =>
        QueryBuilders.rangeQuery("@timestamp").gte(t.toString)
    }
  }

  def searchTermQuery(): Option[QueryBuilder] = {
    currentSearchTerm.get() match {
      case None => None
      case Some(term) =>
        if (term.trim.length < 1) {
          None
        } else {
          Some(QueryBuilders.termQuery("body", term))
        }
    }
  }

  def currentQueries(): Seq[QueryBuilder] = {
    Seq(
      startDateQuery(),
      searchTermQuery()
    ).flatten.toSeq
  }

  def compositeQueries(queries: Seq[QueryBuilder]): QueryBuilder = {
    queries.size match {
      case 0 => QueryBuilders.matchAllQuery()
      case _ =>
        queries.foldLeft(QueryBuilders.boolQuery()) {
          (composite, query) =>
            composite.must(query)
        }
    }
  }

  def queryForRooms(): QueryBuilder = {
    compositeQueries(currentQueries())
  }

  case class RoomUIUpdate(room: Room) extends UILogic {
    def perform(): UIMessage = {
      val messages = Models.messageRepository.search(
        query = compositeQueries(
          currentQueries() :+ QueryBuilders.matchQuery("room", room.roomId.value)
        ),
        options = SearchOptions(
          sort = Some(SortBuilders.fieldSort("@timestamp").order(SortOrder.DESC)),
          size = Some(50)
        )
      )
      Models.markAsReadRepository.get(room.roomId) match {
        case Some(m) =>
          UI.ref ! UpdateRoomMuteCheck(room, selected = m.markAsRead)
        case None =>
          UI.ref ! UpdateRoomMuteCheck(room, selected = false)
      }
      currentRoom.set(room)

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
      roomPane.center = new RoomListViewWithUIUpdate {
        items = ObservableBuffer(rooms)
      }
    }
  }

  case class RoomListUpdate() extends UILogic {
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

  val currentRoom = Reference[Room]()

  val muteCheck = new CheckBox {
    text = "Mute"
    onAction = handle {
      currentRoom.get foreach {
        case room =>
          UI.ref ! MuteRoom(room, this.selected.value)
      }
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

  val roomPane = new BorderPane {
    center = new RoomListViewWithUIUpdate()
  }

  val chatRoomCenterPane = new SplitPane {
    padding = UIStyles.paddingInsets
    dividerPositions_=(0.2, 0.8)
    items ++= Seq(roomPane, messagePane)
  }

  val searchStartSelector = new PeriodStartView {
    override def onSelect(time: TimeValue): Unit = {
      UI.ref ! RoomListUpdate()
    }
  }

  val currentSearchTerm = Reference[String]()

  val searchTerm = new TextField {
    onAction = handle {
      UI.ref ! RoomListUpdate()
    }
    onKeyTyped = handle {
      currentSearchTerm.set(text.value)
    }
    promptText = "Search"
  }

  val searchPane = new HBox {
    padding = UIStyles.paddingInsets
    spacing = UIStyles.spacingWidth
    children = Seq(
      searchStartSelector,
      searchTerm
    )
  }

  val chatRoomPane = new BorderPane {
    padding = UIStyles.paddingInsets
    top = new FlowPane {
      padding = UIStyles.paddingInsets
      children = searchPane
    }
    center = chatRoomCenterPane
  }

}
