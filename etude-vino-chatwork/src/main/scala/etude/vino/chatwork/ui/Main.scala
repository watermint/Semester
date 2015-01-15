package etude.vino.chatwork.ui

import java.time.Instant

import etude.pintxos.chatwork.domain.model.account.AccountId
import etude.pintxos.chatwork.domain.model.message.{Message, MessageId, Text}
import etude.pintxos.chatwork.domain.model.room.{Room, RoomId, RoomTypeDirect}
import etude.vino.chatwork.ui.pane.{AccountList, MessageList, RoomList}

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.Button
import scalafx.scene.layout.BorderPane

object Main extends JFXApp {
  UI.ref ! "startup"

  val rootPane = new BorderPane {

    padding = Insets(UIStyles.spacing)

    left = RoomList.roomListView

    center = MessageList.messageList

    right = AccountList.accountList

    bottom = new Button {
      text = "update"
      onAction = handle {
      }
    }
  }

  val rootScene = new Scene {
    root = rootPane
  }

  stage = new PrimaryStage {
    title = "Vino Chatwork"
    width = 800
    height = 600
    scene = rootScene
    onCloseRequest = handle {
      UI.ref ! "shutdown"
    }
  }
}
