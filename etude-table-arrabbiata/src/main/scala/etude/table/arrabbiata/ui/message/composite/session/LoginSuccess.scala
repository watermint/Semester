package etude.table.arrabbiata.ui.message.composite.session

import etude.table.arrabbiata.ui.UIActor
import etude.table.arrabbiata.ui.message.composite.{CompositeUIMessage, StatusUpdate}

case class LoginSuccess() extends CompositeUIMessage {
  def perform(): Unit = {
    UIActor.ui ! StatusUpdate("Logged in")
  }
}
