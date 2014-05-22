package etude.app.arrabbiata.ui.message

import etude.app.arrabbiata.ui.Main

case class NotificationShow(status: String) extends UIMessage {
  def perform(): Unit = {
    if (!Main.notificationPane.isShowing) {
      Main.notificationPane.show(status)
    }
  }
}
