package etude.table.arrabbiata.ui.message.micro

import etude.table.arrabbiata.ui.Main

private[ui]
case class NotificationHide() extends MicroUIMessage {
  def perform(): Unit = {
    Main.notificationPane.hide()
  }
}
