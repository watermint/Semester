package etude.app.arrabbiata.ui.message.micro

import etude.app.arrabbiata.ui.Main
import etude.foundation.logging.LoggerFactory

private[ui]
case class StatusUpdate(status: String) extends MicroUIMessage {
  val logger = LoggerFactory.getLogger(getClass)

  def perform(): Unit = {
    Main.footerPane.footerLabel.text = status
    logger.info("footer label updated")
  }
}
