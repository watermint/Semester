package etude.vino.chatwork.ui.pane

import java.awt.Desktop

import etude.pintxos.chatwork.domain.model.message.Message
import etude.vino.chatwork.service.api.ApiSession
import etude.vino.chatwork.ui.UIMessage
import etude.vino.chatwork.ui.control.MessageListView

import scalafx.Includes._
import scalafx.collections.ObservableBuffer

object MessageListPane {
  val messageList = new MessageListView()

  val toMeMessageList = new MessageListView() {
    onMouseClicked = handle {
      val message = delegate.getSelectionModel.getSelectedItem
      ApiSession.chatworkIOContextOption match {
        case None =>
        case Some(context) =>
          Desktop.getDesktop.browse(message.uri(context))
      }
    }
  }

  case class MessageListUpdate(messages: Seq[Message]) extends UIMessage {
    def perform(): Unit = {
      messageList.items = ObservableBuffer(messages)
    }
  }

  case class UpdateToMeMessages(messages: Seq[Message]) extends UIMessage {
    def perform(): Unit = {
      toMeMessageList.items = ObservableBuffer(messages)
    }
  }

}
