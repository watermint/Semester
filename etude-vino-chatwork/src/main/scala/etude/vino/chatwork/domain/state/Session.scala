package etude.vino.chatwork.domain.state

import akka.actor.{Actor, Props}
import etude.pintxos.chatwork.domain.service.v0.response.InitLoadResponse
import etude.vino.chatwork.ui.Main.ApplicationReady
import etude.vino.chatwork.ui.UI

class Session extends Actor {
  def receive: Receive = {
    case _: InitLoadResponse =>
      UI.ref ! ApplicationReady()
  }
}

object Session {
  val actorRef = UI.system.actorOf(Props[Session])
}
