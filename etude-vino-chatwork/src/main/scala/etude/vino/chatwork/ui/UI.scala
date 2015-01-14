package etude.vino.chatwork.ui

import akka.actor.{Actor, ActorSystem, Props}
import etude.vino.chatwork.service.Service

import scalafx.application.Platform

case class UI() extends Actor {
  def receive: Receive = {
    case m: UIMessage =>
      Platform.runLater {
        m.perform()
      }

    case "startup" =>
      Service.startup()

    case "shutdown" =>
      Service.shutdown()
      UI.system.shutdown()
  }
}

object UI {
  val system = ActorSystem("cw-vino-ui")

  val ref = system.actorOf(Props[UI])
}
