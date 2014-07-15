package etude.table.arrabbiata.controller.message.session

import etude.table.arrabbiata.controller.message.MessageWithoutSession
import etude.table.arrabbiata.state.Session
import etude.table.arrabbiata.ui.UIActor
import etude.table.arrabbiata.ui.message.composite.session.{LoginSuccess, NoSession}

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
