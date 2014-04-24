package etude.domain.core.event.async

import etude.domain.core.event.{DomainEventSubscriber, DomainEvent}
import scala.concurrent.Future

/**
 * @tparam DE type of domain event.
 * @tparam MR type of monad value.
 */
trait AsyncDomainEventSubscriber[DE <: DomainEvent[_], +MR]
  extends DomainEventSubscriber[DE, Future, MR]
