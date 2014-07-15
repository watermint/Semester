package etude.app.arrabbiata.ui.message.composite.session

import etude.app.arrabbiata.ui.UIActor
import etude.app.arrabbiata.ui.message.composite.{CompositeUIMessage, StatusUpdate}

case class LoginSuccess() extends CompositeUIMessage {
  def perform(): Unit = {
    UIActor.ui ! StatusUpdate("Logged in")
  }
}
