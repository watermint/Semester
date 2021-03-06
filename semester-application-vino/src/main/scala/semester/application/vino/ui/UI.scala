package semester.application.vino.ui

import akka.actor.{Actor, ActorSystem, Props}
import semester.application.vino.service.Service

import scala.concurrent.Future
import scalafx.application.Platform

case class UI() extends Actor {
  implicit val executor = UI.system.dispatcher
  def receive: Receive = {
    case m: UIMessage =>
      Platform.runLater {
        m.perform()
      }

    case l: UILogic =>
      Future {
        l.perform()
      } map {
        m =>
          self ! m
      }

    case "startup" =>
      Service.startup()

    case "shutdown" =>
      Service.shutdown()
      UI.system.terminate()
  }
}

object UI {
  val system = ActorSystem("cw-vino-ui")

  val ref = system.actorOf(Props[UI])
}
