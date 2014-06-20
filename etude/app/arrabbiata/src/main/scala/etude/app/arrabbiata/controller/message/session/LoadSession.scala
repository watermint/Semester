package etude.app.arrabbiata.controller.message.session

import etude.app.arrabbiata.controller.message.MessageWithoutSession
import etude.app.arrabbiata.state.Session
import etude.app.arrabbiata.ui.UIActor
import etude.app.arrabbiata.ui.message.composite.session.{LoginSuccess, NoSession}

import scala.util.{Failure, Success}

case class LoadSession() extends MessageWithoutSession {
  def perform(): Unit = {
    Session.session.get() match {
      case null =>
        Session.fromThinConfig() onComplete {
          case Success(s) =>
            UIActor.ui ! LoginSuccess()
          case Failure(f) =>
            UIActor.ui ! NoSession()
        }
      case s =>
        UIActor.ui ! LoginSuccess()
    }
  }
}
