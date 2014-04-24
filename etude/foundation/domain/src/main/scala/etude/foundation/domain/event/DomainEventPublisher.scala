package etude.foundation.domain.event

import scala.language.higherKinds
import etude.foundation.domain.lifecycle.EntityIOContext

/**
 * @tparam A type of domain event.
 * @tparam M type of monad.
 * @tparam MR type of monad value.
 */
trait DomainEventPublisher[A <: DomainEvent[_], M[+B], MR] {
  type Publisher <: DomainEventPublisher[A, M, MR]

  type Subscriber <: DomainEventSubscriber[A, M, MR]

  def publish(event: A)(implicit context: EntityIOContext[M]): Seq[M[MR]]

  def subscribe(subscriber: Subscriber): Publisher

  def unsubscribe(subscriber: Subscriber): Publisher
}