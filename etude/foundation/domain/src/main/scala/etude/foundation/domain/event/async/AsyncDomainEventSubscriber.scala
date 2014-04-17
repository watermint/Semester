package etude.foundation.domain.event.async

import etude.foundation.domain.event.{DomainEventSubscriber, DomainEvent}
import scala.concurrent.Future

/**
 * @tparam DE type of domain event.
 * @tparam MR type of monad value.
 */
trait AsyncDomainEventSubscriber[DE <: DomainEvent[_], +MR]
  extends DomainEventSubscriber[DE, Future, MR]
