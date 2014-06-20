package etude.app.arrabbiata.controller.message.session

import etude.app.arrabbiata.controller.message.MessageWithoutSession
import etude.app.arrabbiata.state.Session
import etude.app.arrabbiata.ui.UIActor
import etude.app.arrabbiata.ui.message.composite.session.{LoginFailure, LoginSuccess}

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
