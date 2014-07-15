package etude.app.arrabbiata.ui.message.micro

import etude.app.arrabbiata.ui.Main

private[ui]
case class NotificationShow(status: String) extends MicroUIMessage {
  def perform(): Unit = {
    if (!Main.notificationPane.isShowing) {
      Main.notificationPane.show(status)
    }
  }
}
