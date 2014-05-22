package etude.app.arrabbiata.ui.pane

import scalafx.scene.control.{Label, ToolBar}

case class FooterPane() extends ToolBar {

  lazy val footerLabel = new Label("")

  items = Seq(
    footerLabel
  )
}
