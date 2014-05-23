package etude.app.arrabbiata.ui.message.composite

import etude.app.arrabbiata.ui.UIActor
import etude.app.arrabbiata.ui.message.micro.{NotificationShow, StatusUpdate}

case class LoginFailure(cause: Throwable) extends CompositeUIMessage {
  def perform(): Unit = {
    UIActor.ui ! StatusUpdate(s"Login failed: ${cause.getMessage}")
    UIActor.ui ! NotificationShow("Login failed")
  }
}
