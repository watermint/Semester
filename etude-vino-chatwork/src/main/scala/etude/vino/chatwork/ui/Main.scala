package etude.vino.chatwork.ui

import etude.vino.chatwork.ui.pane.{ApplicationLogPane, ChatRoomsPane, MessageListPane}

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Pos
import scalafx.scene.Scene
import scalafx.scene.control.{ProgressIndicator, Tab, TabPane}
import scalafx.scene.layout.{BorderPane, HBox}

object Main extends JFXApp {
  UI.ref ! "startup"

  case class ApplicationReady() extends UIMessage {
    def perform(): Unit = {
      rootPane.center = mainTabPane
    }
  }

  val mainTabPane = new TabPane() {
    tabs = Seq(
      new Tab() {
        text = "Chat Rooms"
        closable = false
        content = ChatRoomsPane.chatRoomPane
      },
      new Tab() {
        text = "Timeline"
        closable = false
        content = MessageListPane.messageTimeline
      },
      new Tab() {
        text = "To/ReplyTo Me"
        closable = false
        content = MessageListPane.toMeMessageList
      },
      new Tab() {
        text = "Application log"
        content = ApplicationLogPane.applicationLog
        closable = false
      }
    )
  }

  val rootPane = new BorderPane {

    padding = UIStyles.paddingInsets

    center = new HBox {
      alignment = Pos.Center
      children = new ProgressIndicator {
        prefWidth = 30
        prefHeight = 30
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