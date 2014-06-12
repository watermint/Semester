package etude.app.arrabbiata.controller.message

import etude.app.arrabbiata.controller.Session
import etude.app.arrabbiata.ui.UIActor
import etude.app.arrabbiata.ui.message.composite.{LoginSuccess, NoSession}

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
      case _ =>
        UIActor.ui ! LoginSuccess()
    }
  }
}
