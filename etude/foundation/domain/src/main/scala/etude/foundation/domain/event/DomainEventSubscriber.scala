package etude.foundation.domain.event

import scala.language.higherKinds
import etude.foundation.domain.lifecycle.EntityIOContext

/**
 * @tparam E type of domain event.
 * @tparam M type of monad.
 * @tparam MR type of monad value.
 */
trait DomainEventSubscriber[E <: DomainEvent[_], M[+A], +MR] {
  def handleEvent(event: E)(implicit context: EntityIOContext[M]): M[MR]
}
