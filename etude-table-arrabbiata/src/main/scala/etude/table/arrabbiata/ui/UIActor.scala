package etude.table.arrabbiata.ui

import akka.actor.{Actor, ActorSystem, Props}
import etude.table.arrabbiata.ui.message.UIMessage
import etude.epice.logging.LoggerFactory

import scalafx.application.Platform

class UIActor extends Actor {
  def receive = {
    case m: UIMessage =>
      UIActor.logger.info(s"UI message: $m")
      Platform.runLater {
        m.perform()
      }
  }
}

object UIActor {
  val logger = LoggerFactory.getLogger(getClass)

  val system = ActorSystem("UI")

  val ui = system.actorOf(Props[UIActor], name = "UI")
}