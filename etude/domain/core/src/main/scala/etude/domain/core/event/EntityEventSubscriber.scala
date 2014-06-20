package etude.domain.core.event

import etude.domain.core.model.{Entity, Identity}

import scala.language.higherKinds

trait EntityEventSubscriber[ID <: Identity[_], E <: Entity[ID], M[+B]]
  extends DomainEventSubscriber[EntityEvent[ID, E], M, E]
