package etude.app.arrabbiata.ui.message

import etude.app.arrabbiata.ui.Main
import etude.foundation.logging.LoggerFactory

case class StatusUpdate(status: String) extends UIMessage {
  val logger = LoggerFactory.getLogger(getClass)

  def perform(): Unit = {
    Main.footerPane.footerLabel.text = status
    logger.info("footer label updated")
  }
}
