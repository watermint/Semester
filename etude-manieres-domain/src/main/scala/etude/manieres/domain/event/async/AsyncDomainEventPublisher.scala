package etude.manieres.domain.event.async

import etude.manieres.domain.event.{DomainEvent, DomainEventPublisher}

import scala.concurrent.Future

/**
 * @tparam A type of domain event.
 * @tparam MR type of monad value.
 */
trait AsyncDomainEventPublisher[A <: DomainEvent[_], MR]
  extends DomainEventPublisher[A, Future, MR]
