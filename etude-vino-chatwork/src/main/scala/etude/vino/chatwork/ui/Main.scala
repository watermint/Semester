package etude.vino.chatwork.ui

import etude.vino.chatwork.ui.pane.{RoomListPane, ApplicationLogPane, MessageListPane}

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.control.{Tab, TabPane}
import scalafx.scene.layout.{Priority, HBox, BorderPane}

object Main extends JFXApp {
  UI.ref ! "startup"

  val rootPane = new BorderPane {

    padding = UIStyles.paddingInsets

    center = new TabPane() {
      tabs = Seq(
        new Tab() {
          text = "Chat Rooms"
          closable = false
          content = new HBox {
            spacing = UIStyles.spacingWidth
            hgrow = Priority.Always
            vgrow = Priority.Always
            children = Seq(
              RoomListPane.roomListView,
              MessageListPane.timelineOfRoom
            )
          }
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