package etude.app.arrabbiata.ui.message.composite.session

import etude.app.arrabbiata.ui.UIActor
import etude.app.arrabbiata.ui.message.composite.{CompositeUIMessage, StatusUpdate}
import etude.app.arrabbiata.ui.message.micro.NotificationShow

case class NoSession() extends CompositeUIMessage {
  def perform(): Unit = {
    UIActor.ui ! StatusUpdate("Login required")
    UIActor.ui ! NotificationShow("Login required")
  }
}
