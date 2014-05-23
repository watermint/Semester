package etude.app.arrabbiata.ui.pane

import scalafx.scene.layout.HBox
import etude.app.arrabbiata.ui.UI
import etude.foundation.logging.LoggerFactory

case class CenterPane() extends HBox with UI {
  val logger = LoggerFactory.getLogger(getClass)

  content = Seq(
  )
}
