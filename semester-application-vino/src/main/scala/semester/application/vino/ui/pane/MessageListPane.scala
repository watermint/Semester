package semester.application.vino.ui.pane

import semester.service.chatwork.domain.model.message.Message
import semester.application.vino.ui.UIMessage
import semester.application.vino.ui.control.MessageListView

import scalafx.collections.ObservableBuffer
import scalafx.scene.layout.Priority

object MessageListPane {
  val timelineOfRoom = new MessageListView() {
    hgrow = Priority.Always
    vgrow = Priority.Always
  }

  val messageTimeline = new MessageListView()

  val toMeMessageList = new MessageListView()

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
