package etude.table.arrabbiata.ui.pane

import etude.table.arrabbiata.ui.UI
import etude.table.arrabbiata.ui.pane.room.MergeRoomPane
import etude.epice.logging.LoggerFactory

import scalafx.geometry.Pos
import scalafx.scene.control.{Tab, TabPane}
import scalafx.scene.layout.Priority

object CenterPane extends TabPane with UI {
  val logger = LoggerFactory.getLogger(getClass)

  hgrow = Priority.ALWAYS
  vgrow = Priority.ALWAYS

  tabs = Seq(
    new Tab {
      alignmentInParent = Pos.CENTER
      text = "Merge Room"
      content = MergeRoomPane
      closable = false
    }
  )
}
