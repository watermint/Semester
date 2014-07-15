package etude.table.arrabbiata.ui.pane

import scalafx.scene.control.{Label, ToolBar}

object FooterPane extends ToolBar {
  lazy val footerLabel = new Label("")

  items = Seq(
    footerLabel
  )
}
