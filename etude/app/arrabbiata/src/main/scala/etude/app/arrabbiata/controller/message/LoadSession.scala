package etude.app.arrabbiata.controller.message

import etude.app.arrabbiata.controller.Session
import etude.app.arrabbiata.ui.UIActor
import etude.app.arrabbiata.ui.message.composite.NoSession

case class LoadSession() extends MessageWithoutSession {
  def perform(): Unit = {
    Session.session.get() match {
      case null =>
        UIActor.ui ! NoSession()
      case _ =>
    }
  }
}
