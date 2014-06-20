package etude.app.arrabbiata.ui.message.composite

import etude.app.arrabbiata.ui.Main
import etude.app.arrabbiata.ui.message.micro.MicroUIMessage
import etude.foundation.logging.LoggerFactory

case class StatusUpdate(status: String) extends CompositeUIMessage {
  val logger = LoggerFactory.getLogger(getClass)

  def perform(): Unit = {
    Main.footerPane.footerLabel.text = status
    logger.info("footer label updated")
  }
}
