package etude.manieres.domain.event

import akka.actor.ActorRef

trait Publisher[A <: DomainEvent[_]] {
  val router: ActorRef

  def publish(event: A) = {
    router ! event
  }
}
