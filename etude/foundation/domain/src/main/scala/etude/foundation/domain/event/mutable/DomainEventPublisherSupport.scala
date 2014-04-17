package etude.foundation.domain.event.mutable

import etude.foundation.domain.event.{DomainEventPublisher, DomainEvent}
import scala.collection.mutable.ArrayBuffer
import etude.foundation.domain.lifecycle.EntityIOContext

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
