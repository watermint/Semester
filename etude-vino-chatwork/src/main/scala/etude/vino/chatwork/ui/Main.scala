package etude.vino.chatwork.ui

import java.time.Instant

import etude.pintxos.chatwork.domain.model.account.AccountId
import etude.pintxos.chatwork.domain.model.message.{Message, MessageId, Text}
import etude.pintxos.chatwork.domain.model.room.{Room, RoomId, RoomTypeDirect}
import etude.vino.chatwork.ui.pane.{MessageList, RoomList}

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

    bottom = new Button {
      text = "update"
      onAction = handle {
        val rooms = Seq(
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
        RoomList.roomListView.items = ObservableBuffer(rooms)

        val messages = Seq(
          new Message(
            MessageId(RoomId(123), 1000),
            AccountId(4001223),
            Text("ABC 1000 ABC 1000 ABC 1000 ABC 1000 ABC 1000 ABC 1000 ABC 1000 ABC 1000 ABC 1000 ABC 1000 ABC 1000 ABC 1000 ABC 1000 ABC 1000 ABC 1000 ABC 1000 ABC 1000 ABC 1000 ABC 1000 ABC 1000 ABC 1000 ABC 1000 ABC 1000 ABC 1000 ABC 1000 ABC 1000"),
            Instant.now,
            None
          ),
          new Message(
            MessageId(RoomId(456), 2000),
            AccountId(400456),
            Text("ABC 2000"),
            Instant.now,
            None
          )
        )

        MessageList.messageList.items = ObservableBuffer(messages)
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
