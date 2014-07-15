package etude.app.arrabbiata.ui.pane.room

import etude.app.arrabbiata.controller.AppActor
import etude.app.arrabbiata.controller.message.room.LoadRoomList
import etude.app.arrabbiata.ui.control.{AccountListView, RoomListView}
import etude.app.arrabbiata.ui.message.composite.room.{MergeRoom, SelectMergeRooms, UpdateMergeRoomLists}
import etude.app.arrabbiata.ui.{UI, UIActor, UIUnit}
import etude.pintxos.chatwork.domain.model.account.Account
import etude.pintxos.chatwork.domain.model.room.Room

import scalafx.collections.ObservableBuffer
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.{Button, Label, TextField}
import scalafx.scene.layout.{HBox, Priority, VBox}

object MergeRoomPane extends HBox with UI {

  val searchRoomBase = new TextField {
    promptText = "Search Base Room"
    onKeyPressed = event {
      e =>
        UIActor.ui ! UpdateMergeRoomLists()
    }
  }
  val baseRoomItems = new ObservableBuffer[Room]()
  val baseRoomList = new RoomListView {
    items = baseRoomItems
    hgrow = Priority.ALWAYS
    vgrow = Priority.ALWAYS
    override def onSelected(room: Room): Unit = {
      UIActor.ui ! SelectMergeRooms()
    }
  }

  val searchRoomTarget = new TextField {
    promptText = "Search Target Room"
    onKeyPressed = event {
      e =>
        UIActor.ui ! UpdateMergeRoomLists()
    }
  }
  val targetRoomItems = new ObservableBuffer[Room]()
  val targetRoomList = new RoomListView {
    items = targetRoomItems
    hgrow = Priority.ALWAYS
    vgrow = Priority.ALWAYS
    override def onSelected(room: Room): Unit = {
      UIActor.ui ! SelectMergeRooms()
    }
  }
  val diffParticipantItems = new ObservableBuffer[Account]()
  val diffParticipantList = new AccountListView {
    items = diffParticipantItems
    hgrow = Priority.ALWAYS
    vgrow = Priority.ALWAYS
  }
  val mergeButton = new Button {
    text = "Merge"
    disable = true
    onAction = event {
      e =>
        UIActor.ui ! MergeRoom()
    }
  }

  AppActor.app ! LoadRoomList(UpdateMergeRoomLists())

  hgrow = Priority.ALWAYS
  vgrow = Priority.ALWAYS

  padding = Insets(UIUnit.spacing)
  spacing = UIUnit.spacing
  content = Seq(
    // Base room
    new VBox {
      alignment = Pos.CENTER
      hgrow = Priority.ALWAYS
      vgrow = Priority.ALWAYS
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
      hgrow = Priority.ALWAYS
      vgrow = Priority.ALWAYS
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
      hgrow = Priority.ALWAYS
      vgrow = Priority.ALWAYS
      spacing = UIUnit.spacing
      content = Seq(
        new Label {
          text = "Account(s)"
        },
        diffParticipantList,
        mergeButton
      )
    }
  )
}
