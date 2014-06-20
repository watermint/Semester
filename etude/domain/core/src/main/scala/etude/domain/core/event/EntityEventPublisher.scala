package etude.domain.core.event

import etude.domain.core.model.{Entity, Identity}

import scala.language.higherKinds

trait EntityEventPublisher[ID <: Identity[_], E <: Entity[ID], M[+B]]
  extends DomainEventPublisher[EntityEvent[ID, E], M, E]
