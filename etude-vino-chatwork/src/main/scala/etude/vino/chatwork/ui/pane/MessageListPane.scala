package etude.vino.chatwork.ui.pane

import etude.pintxos.chatwork.domain.model.message.Message
import etude.vino.chatwork.ui.UIMessage
import etude.vino.chatwork.ui.control.MessageListView

import scalafx.collections.ObservableBuffer
import scalafx.scene.layout.Priority

object MessageListPane {
  val timelineOfRoom = new MessageListView() {
    hgrow = Priority.Always
    vgrow = Priority.Always
  }

  val messageTimeline = new MessageListView()

  val toMeMessageList = new MessageListView()

  case class UpdateTimelineForRoom(messages: Seq[Message]) extends UIMessage {
    def perform(): Unit = {
      timelineOfRoom.items = ObservableBuffer(messages)
    }
  }
  
  case class UpdateTimeline(messages: Seq[Message]) extends UIMessage {
    def perform(): Unit = {
      messageTimeline.items = ObservableBuffer(messages)
    }
  }

  case class UpdateToMeMessages(messages: Seq[Message]) extends UIMessage {
    def perform(): Unit = {
      toMeMessageList.items = ObservableBuffer(messages)
    }
  }

}
