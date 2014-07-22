package etude.manieres.domain.event

import etude.manieres.domain.model.{Entity, Identity}

import scala.language.higherKinds

trait EntityEventSubscriber[ID <: Identity[_], E <: Entity[ID], M[+B]]
  extends DomainEventSubscriber[EntityEvent[ID, E], M, E]
