package etude.app.arrabbiata.controller

import akka.actor.{Props, ActorSystem, Actor}
import etude.app.arrabbiata.controller.message.{MessageWithoutSession, MessageWithSession}
import etude.app.arrabbiata.ui.UIActor
import etude.app.arrabbiata.ui.message.{NoSession, NotificationShow}

class AppActor extends Actor {
  def noSession(): Unit = {
    UIActor.ui ! NoSession()
  }

  def receive = {
    case e: MessageWithoutSession => e.perform()
    case e: MessageWithSession =>
      Session.session.get() match {
        case null =>
          noSession()
        case s =>
          e.perform(s)
      }
  }
}

object AppActor {
  val system = ActorSystem("session")

  val app = system.actorOf(Props[AppActor], name = "session")
}
