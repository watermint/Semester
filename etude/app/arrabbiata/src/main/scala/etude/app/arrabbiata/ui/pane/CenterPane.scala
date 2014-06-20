package etude.app.arrabbiata.ui.pane

import etude.app.arrabbiata.ui.UI
import etude.foundation.logging.LoggerFactory

import scalafx.scene.control.TabPane

case class CenterPane() extends TabPane with UI {
  val logger = LoggerFactory.getLogger(getClass)

}
