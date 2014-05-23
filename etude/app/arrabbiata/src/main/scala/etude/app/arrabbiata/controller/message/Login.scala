package etude.app.arrabbiata.controller.message

import etude.app.arrabbiata.controller.{AppActor, Session}
import etude.app.arrabbiata.ui.UIActor
import etude.app.arrabbiata.ui.message.composite.{LoginFailure, LoginSuccess}
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
