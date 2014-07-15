package etude.table.arrabbiata.controller.message.session

import etude.table.arrabbiata.controller.message.MessageWithoutSession
import etude.table.arrabbiata.state.Session
import etude.table.arrabbiata.ui.UIActor
import etude.table.arrabbiata.ui.message.composite.session.{LoginFailure, LoginSuccess}

import scala.util.{Failure, Success}

case class Login(username: String, password: String, orgId: String) extends MessageWithoutSession {
  def perform(): Unit = {

    Session.login(username, password, orgId) onComplete {
      case Success(s) =>
        UIActor.ui ! LoginSuccess()
      case Failure(t) =>
        UIActor.ui ! LoginFailure(t)
    }
  }
}
