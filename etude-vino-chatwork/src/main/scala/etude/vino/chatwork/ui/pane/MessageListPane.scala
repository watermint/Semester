package etude.vino.chatwork.ui.pane

import etude.pintxos.chatwork.domain.model.message.Message
import etude.vino.chatwork.ui.UIMessage
import etude.vino.chatwork.ui.control.MessageListView

import scalafx.collections.ObservableBuffer

object MessageListPane {
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
