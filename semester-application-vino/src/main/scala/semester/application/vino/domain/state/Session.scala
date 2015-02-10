package semester.application.vino.domain.state

import akka.actor.{Actor, Props}
import semester.service.chatwork.domain.service.response.InitLoadResponse
import semester.application.vino.ui.Main.ApplicationReady
import semester.application.vino.ui.UI

class Session extends Actor {
  def receive: Receive = {
    case _: InitLoadResponse =>
      UI.ref ! ApplicationReady()
  }
}

object Session {
  val actorRef = UI.system.actorOf(Props[Session])
}
