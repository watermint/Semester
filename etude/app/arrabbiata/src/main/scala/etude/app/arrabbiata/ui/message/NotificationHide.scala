package etude.app.arrabbiata.ui.message

import etude.app.arrabbiata.ui.Main

case class NotificationHide() extends UIMessage {
  def perform(): Unit = {
    Main.notificationPane.hide()
  }
}
