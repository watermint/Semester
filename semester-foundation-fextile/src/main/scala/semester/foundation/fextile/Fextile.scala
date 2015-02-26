package semester.foundation.fextile

import akka.actor.{Props, ActorSystem, Actor}

class Fextile extends Actor {
  def receive: Receive = {
    case _ =>
  }
}

object Fextile {
  val system = ActorSystem("fextile")

  val ref = system.actorOf(Props[Fextile])
}
