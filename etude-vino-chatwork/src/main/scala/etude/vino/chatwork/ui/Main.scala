package etude.vino.chatwork.ui

import etude.vino.chatwork.ui.pane.ApplicationLogPane

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.layout.BorderPane

object Main extends JFXApp {
  UI.ref ! "startup"

  val rootPane = new BorderPane {

    padding = Insets(UIStyles.spacing)

    center = ApplicationLogPane.applicationLog
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