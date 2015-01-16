package etude.vino.chatwork.ui

import etude.vino.chatwork.ui.control.PeriodStartView
import etude.vino.chatwork.ui.pane.{AccountList, MessageList, RoomList}

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.layout.{BorderPane, HBox}

object Main extends JFXApp {
  //UI.ref ! "startup"

  val button: Button = new PeriodStartView

  val rootPane = new BorderPane {

    padding = Insets(UIStyles.spacing)

    left = RoomList.roomListView

    center = MessageList.messageList

    right = AccountList.accountList

    bottom = new HBox {
      content = Seq(
        button
      )
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
      //      UI.ref ! "shutdown"
    }
  }
}