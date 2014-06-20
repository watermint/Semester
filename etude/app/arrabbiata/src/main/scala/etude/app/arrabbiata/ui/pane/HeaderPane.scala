package etude.app.arrabbiata.ui.pane

import scalafx.scene.control.{Label, ToolBar}
import scalafx.scene.layout.{Priority, Region}

case class HeaderPane() extends ToolBar {
  items = Seq(
    Label("Arrabbiata"),
    new Region {
      hgrow = Priority.ALWAYS
      minWidth = Region.USE_PREF_SIZE
    }
  )
}
