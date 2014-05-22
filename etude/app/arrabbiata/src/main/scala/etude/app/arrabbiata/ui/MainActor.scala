package etude.app.arrabbiata.ui

import akka.actor.{Props, ActorSystem, Actor}
import etude.app.arrabbiata.ui.message.{StatusUpdate, NotificationShow, NotificationHide}
import scalafx.application.Platform

class MainActor extends Actor {
  def dispatch: PartialFunction[Any, Unit] = {
    case s: StatusUpdate => MainStage.updateFooter(s)
    case s: NotificationShow => MainStage.notify(s)
    case s: NotificationHide => MainStage.hideNotification()
  }

  def receive = {
    case a: Any =>
      Platform.runLater {
        dispatch(a)
      }
  }
}

object MainActor {
  val system = ActorSystem("UI")

  val ui = system.actorOf(Props[MainActor], name = "UI")
}