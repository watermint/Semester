package etude.app.arrabbiata.ui

import akka.actor.{Props, ActorSystem, Actor}
import etude.app.arrabbiata.ui.message.{UIMessage, StatusUpdate, NotificationShow, NotificationHide}
import scalafx.application.Platform
import etude.foundation.logging.LoggerFactory

class MainActor extends Actor {
  def receive = {
    case m: UIMessage =>
      MainActor.logger.info(s"message: $m")
      Platform.runLater {
        m.perform()
      }
  }
}

object MainActor {
  val logger = LoggerFactory.getLogger(getClass)

  val system = ActorSystem("UI")

  val ui = system.actorOf(Props[MainActor], name = "UI")
}