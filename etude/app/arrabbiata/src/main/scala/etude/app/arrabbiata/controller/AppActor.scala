package etude.app.arrabbiata.controller

import akka.actor.{Props, ActorSystem, Actor}
import etude.app.arrabbiata.controller.message.Action

class AppActor extends Actor {
  def receive = {
    case e: Action => e.perform()
  }
}

object AppActor {
  val system = ActorSystem("session")

  val app = system.actorOf(Props[AppActor], name = "session")
}
