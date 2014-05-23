package etude.app.arrabbiata.ui.message.composite

import etude.app.arrabbiata.ui.UIActor
import etude.app.arrabbiata.ui.message.micro.{NotificationShow, StatusUpdate}

case class NoSession() extends CompositeUIMessage {
  def perform(): Unit = {
    UIActor.ui ! StatusUpdate("Login required")
    UIActor.ui ! NotificationShow("Login required")
  }
}
