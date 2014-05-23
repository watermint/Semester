package etude.app.arrabbiata.ui

import akka.actor.{Props, ActorSystem, Actor}
import etude.app.arrabbiata.ui.message.UIMessage
import scalafx.application.Platform
import etude.foundation.logging.LoggerFactory

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