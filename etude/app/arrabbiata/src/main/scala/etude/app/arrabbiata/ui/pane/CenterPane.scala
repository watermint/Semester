package etude.app.arrabbiata.ui.pane

import etude.app.arrabbiata.ui.UI
import etude.app.arrabbiata.ui.pane.room.MergeRoomPane
import etude.foundation.logging.LoggerFactory

import scalafx.geometry.Pos
import scalafx.scene.control.{Tab, TabPane}

case class CenterPane() extends TabPane with UI {
  val logger = LoggerFactory.getLogger(getClass)

  tabs = Seq(
    new Tab {
      alignmentInParent = Pos.CENTER
      text = "Merge Room"
      content = new MergeRoomPane()
    }
  )
}
