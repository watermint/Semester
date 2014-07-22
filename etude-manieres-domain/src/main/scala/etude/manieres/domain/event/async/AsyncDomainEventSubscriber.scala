package etude.manieres.domain.event.async

import etude.manieres.domain.event.{DomainEvent, DomainEventSubscriber}

import scala.concurrent.Future

/**
 * @tparam DE type of domain event.
 * @tparam MR type of monad value.
 */
trait AsyncDomainEventSubscriber[DE <: DomainEvent[_], +MR]
  extends DomainEventSubscriber[DE, Future, MR]
