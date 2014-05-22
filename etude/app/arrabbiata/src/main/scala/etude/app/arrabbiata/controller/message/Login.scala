package etude.app.arrabbiata.controller.message

import etude.app.arrabbiata.ui.UIActor
import etude.app.arrabbiata.ui.message.{StatusUpdate, NotificationShow}

case class Login(username: String, password: String, orgId: String) extends MessageWithoutSession {
  def perform(): Unit = {
    UIActor.ui ! NotificationShow("Login failed")
    UIActor.ui ! StatusUpdate("Login failed")
  }
}
