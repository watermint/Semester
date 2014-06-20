package etude.app.arrabbiata.ui.message.composite.session

import etude.app.arrabbiata.ui.UIActor
import etude.app.arrabbiata.ui.message.composite.CompositeUIMessage
import etude.app.arrabbiata.ui.message.micro.StatusUpdate

case class LoginSuccess() extends CompositeUIMessage {
  def perform(): Unit = {
    UIActor.ui ! StatusUpdate("Logged in")
  }
}