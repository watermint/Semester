package etude.vino.chatwork.ui.pane

import etude.pintxos.chatwork.domain.model.message.Message
import etude.vino.chatwork.ui.UIMessage
import etude.vino.chatwork.ui.control.MessageListView

import scalafx.collections.ObservableBuffer

object MessageList {
  val messageList = new MessageListView()

  case class MessageListUpdate(messages: Seq[Message]) extends UIMessage {
    def perform(): Unit = {
      messageList.items = ObservableBuffer(messages)
    }
  }
}
