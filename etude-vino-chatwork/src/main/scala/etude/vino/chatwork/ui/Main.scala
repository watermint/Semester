package etude.vino.chatwork.ui

import java.time.chrono.Chronology
import java.util.Locale

import etude.vino.chatwork.ui.pane.{AccountList, MessageList, RoomList}
import org.controlsfx.control.PopOver

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.layout.{BorderPane, GridPane, HBox}
import scalafx.scene.shape.Rectangle

object Main extends JFXApp {
  //UI.ref ! "startup"

  val popOver = new PopOver()

  val rect = new Rectangle()

  val startGroup = new ToggleGroup()

  popOver.setContentNode(
    new GridPane {
      padding = Insets(UIStyles.spacing)
      hgap = UIStyles.spacing
      vgap = UIStyles.spacing

      add(
        child = new Label {
          text = "Date/Time from"
        },
        columnIndex = 0,
        rowIndex = 0,
        colspan = 2,
        rowspan = 1
      )
      add(
        child = new RadioButton {
          text = "No constraints"
          toggleGroup = startGroup
        },
        columnIndex = 0,
        rowIndex = 1,
        colspan = 2,
        rowspan = 1
      )
      add(
        child = new RadioButton {
          text = "Date"
          toggleGroup = startGroup
        },
        columnIndex = 0,
        rowIndex = 2
      )
      add(
        child = new DatePicker {
          chronology = Chronology.ofLocale(Locale.JAPANESE)
        },
        columnIndex = 1,
        rowIndex = 2
      )
      add(
        child = new RadioButton {
          text = "Minutes"
          toggleGroup = startGroup
        },
        columnIndex = 0,
        rowIndex = 3
      )
      add(
        child = new ComboBox[Int] {
          editable = true
          items = ObservableBuffer(
            15,
            30
          )
        },
        columnIndex = 1,
        rowIndex = 3
      )
      add(
        child = new RadioButton {
          text = "Hour"
          toggleGroup = startGroup
        },
        columnIndex = 0,
        rowIndex = 4
      )
      add(
        child = new ComboBox[Int] {
          editable = true
          items = ObservableBuffer(
            1,
            2,
            4,
            8,
            24
          )
        },
        columnIndex = 1,
        rowIndex = 4
      )
    }.delegate
  )

  val buttonPopup = new UIMessage {
    def perform(): Unit = {
      popOver.show(button)
    }
  }

  val button: Button = new Button {
    text = "Start Date/Time"
    onAction = handle {
      UI.ref ! buttonPopup
    }
  }

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