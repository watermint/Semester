package etude.vino.chatwork.ui

import etude.vino.chatwork.ui.pane.{MessageListPane, ApplicationLogPane}

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.{TextField, Tab, TabPane}
import scalafx.scene.layout.{Priority, VBox, BorderPane}

object Main extends JFXApp {
  UI.ref ! "startup"

  val rootPane = new BorderPane {

    padding = UIStyles.paddingInsets

    center = new TabPane() {
      tabs = Seq(
        new Tab() {
          text = "Messages to Me"
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