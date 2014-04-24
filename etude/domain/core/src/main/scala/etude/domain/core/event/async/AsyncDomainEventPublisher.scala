package etude.domain.core.event.async

import etude.domain.core.event.{DomainEventPublisher, DomainEvent}
import scala.concurrent.Future

/**
 * @tparam A type of domain event.
 * @tparam MR type of monad value.
 */
trait AsyncDomainEventPublisher[A <: DomainEvent[_], MR]
  extends DomainEventPublisher[A, Future, MR]
