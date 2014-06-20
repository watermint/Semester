package etude.app.arrabbiata.ui

import etude.app.arrabbiata.ui.message.micro.{LoginShow, StatusUpdate}

import scalafx.scene.control.{Menu, MenuBar, MenuItem}

case class MainMenu() extends MenuBar with UI {
  val sessionMenu = new Menu("Session") {
    items = Seq(
      new MenuItem("Login") {
        onAction = event {
          e =>
            UIActor.ui ! LoginShow()
        }
      }
    )
  }

  val roomMenu = new Menu("Room") {
    items = Seq(
      new MenuItem("Merge") {
        onAction = event {
          e =>
            UIActor.ui ! StatusUpdate("Merge rooms")
        }
      }
    )
  }

  useSystemMenuBar = true
  menus = Seq(
    sessionMenu,
    roomMenu
  )
}
