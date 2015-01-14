package etude.vino.chatwork.ui

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage

object Main extends JFXApp {
  stage = new PrimaryStage {
    title = "Vino Chatwork"
    width = 800
    height = 600
    onCloseRequest = handle {
      UI.ref ! "shutdown"
    }
  }
}
