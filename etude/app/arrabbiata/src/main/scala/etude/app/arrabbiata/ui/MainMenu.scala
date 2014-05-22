package etude.app.arrabbiata.ui

import scalafx.scene.control.{MenuItem, Menu, MenuBar}
import etude.app.arrabbiata.ui.message.StatusUpdate

case class MainMenu() extends MenuBar with UI {
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
    roomMenu
  )
}
