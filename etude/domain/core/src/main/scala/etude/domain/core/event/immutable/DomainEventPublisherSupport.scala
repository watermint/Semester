package etude.domain.core.event.immutable

import scala.language.higherKinds
import etude.domain.core.lifecycle.EntityIOContext
import etude.domain.core.event.{DomainEventPublisher, DomainEvent}

/**
 * @tparam A type of domain event.
 * @tparam M type of monad.
 * @tparam MR type of monad value.
 */
trait DomainEventPublisherSupport[A <: DomainEvent[_], M[+B], MR]
  extends DomainEventPublisher[A, M, MR] {

  protected val subscribers: Seq[Subscriber]

  protected def createInstance(subscribers: Seq[Subscriber]): Publisher

  def publish(event: A)(implicit ctx: EntityIOContext[M]): Seq[M[MR]] = {
    subscribers.map(_.handleEvent(event))
  }

  def subscribe(subscriber: Subscriber): Publisher = {
    createInstance(subscribers :+ subscriber)
  }

  def unsubscribe(subscriber: Subscriber): Publisher = {
    createInstance(subscribers.filterNot(_ == subscriber))
  }
}
