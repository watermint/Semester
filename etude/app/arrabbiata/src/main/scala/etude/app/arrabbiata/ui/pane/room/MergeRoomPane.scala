package etude.app.arrabbiata.ui.pane.room

import etude.app.arrabbiata.ui.{UI, UIUnit}

import scalafx.geometry.{Pos, Insets}
import scalafx.scene.control.{Button, Label, ListView, TextField}
import scalafx.scene.layout.{HBox, VBox}

class MergeRoomPane extends HBox with UI {
  val searchRoomBase = new TextField {
    promptText = "Search Base Room"
  }
  val baseRoomList = new ListView {

  }
  val searchRoomTarget = new TextField {
    promptText = "Search Target Room"
  }
  val targetRoomList = new ListView {

  }
  val diffParticipantList = new ListView {

  }

  padding = Insets(UIUnit.spacing)
  spacing = UIUnit.spacing
  content = Seq(
  // Base room
    new VBox {
      alignment = Pos.CENTER
      spacing = UIUnit.spacing
      content = Seq(
        new Label {
          text = "Base Room"
        },
        searchRoomBase,
        baseRoomList
      )
    },

  // Target room
    new VBox {
      alignment = Pos.CENTER
      spacing = UIUnit.spacing
      content = Seq(
        new Label {
          text = "Target Room"
        },
        searchRoomTarget,
        targetRoomList
      )
    },

  // Diff & Merge
    new VBox {
      alignment = Pos.CENTER
      spacing = UIUnit.spacing
      content = Seq(
        new Label {
          text = "Account(s) Diff"
        },
        diffParticipantList,
        new Button {
          text = "Merge"
          disable = true
        }
      )
    }
  )
}
