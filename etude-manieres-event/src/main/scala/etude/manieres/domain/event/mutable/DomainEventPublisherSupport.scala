package etude.manieres.domain.event.mutable

import etude.manieres.domain.event.{DomainEvent, DomainEventPublisher}
import etude.manieres.domain.lifecycle.EntityIOContext

import scala.collection.mutable.ArrayBuffer

trait DomainEventPublisherSupport[A <: DomainEvent[_], M[+B], MR]
  extends DomainEventPublisher[A, M, MR] {

  protected val subscribers: ArrayBuffer[Subscriber]

  def publish(event: A)(implicit ctx: EntityIOContext[M]): Seq[M[MR]] = {
    subscribers.map(_.handleEvent(event))
  }

  def subscribe(subscriber: Subscriber): Publisher = {
    subscribers += subscriber
    this.asInstanceOf[Publisher]
  }

  def unsubscribe(subscriber: Subscriber): Publisher = {
    subscribers -= subscriber
    this.asInstanceOf[Publisher]
  }
}
