package etude.manieres.domain.event

import akka.actor._

class Router extends Actor {
  private var listeners = Seq.empty[ActorRef]

  def receive: Receive = {
    case b: Router.Subscribe =>
      listeners :+= b.listener

  }
}

object Router {
  trait Command

  case class Subscribe(listener: ActorRef) extends Command

  def apply()(implicit system: ActorSystem): ActorRef = system.actorOf(
    props = Props[Router],
    name = "Domain-Event"
  )
}
