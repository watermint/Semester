package etude.table.arrabbiata.ui.message.composite

import etude.table.arrabbiata.ui.Main
import etude.epice.logging.LoggerFactory

case class StatusUpdate(status: String) extends CompositeUIMessage {
  val logger = LoggerFactory.getLogger(getClass)

  def perform(): Unit = {
    Main.footerPane.footerLabel.text = status
    logger.info("footer label updated")
  }
}
