package etude.vino.chatwork.ui

import java.net.URI

import etude.pintxos.chatwork.domain.model.room.{RoomTypeDirect, RoomId, Room}
import etude.vino.chatwork.ui.pane.RoomList

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.Button
import scalafx.scene.layout.BorderPane

object Main extends JFXApp {
  //  UI.ref ! "startup"

  val rootPane = new BorderPane {
    padding = Insets(UIStyles.spacing)

    left = RoomList.roomListView

    bottom = new Button {
      text = "update"
      onAction = handle {
        val items = Seq(
          new Room(
            RoomId(123),
            "test123",
            None,
            None,
            RoomTypeDirect(),
            None,
            None
          ),
          new Room(
            RoomId(456),
            "test456",
            None,
            None,
            RoomTypeDirect(),
            None,
            None
          ),
          new Room(
            RoomId(789),
            "test789",
            None,
            None,
            RoomTypeDirect(),
            None,
            None
          )

        )
        RoomList.roomListView.items = ObservableBuffer(items)
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
      //      UI.ref ! "shutdown"
    }
  }
}
