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
//  UI.ref ! "startup"

  val rootPane = new BorderPane {

    padding = Insets(UIStyles.spacing)

    center = new TabPane() {
      tabs = Seq(
        new Tab() {
          text = "Message Search"
          closable = false
          content = new VBox {
            spacing = UIStyles.spacing
            padding = Insets(UIStyles.padding)
            hgrow = Priority.Always
            vgrow = Priority.Always
            content = Seq(
              new TextField {
                onAction = handle {

                }
              },
              MessageListPane.messageList
            )
          }
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
//      UI.ref ! "shutdown"
    }
  }
}