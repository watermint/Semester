package etude.app.arrabbiata.ui.message.micro

import etude.app.arrabbiata.ui.Main

private[ui]
case class NotificationHide() extends MicroUIMessage {
  def perform(): Unit = {
    Main.notificationPane.hide()
  }
}
