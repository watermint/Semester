package etude.manieres.domain.event

import etude.manieres.domain.model.{Entity, Identity}

import scala.language.higherKinds

trait EntityEventPublisher[ID <: Identity[_], E <: Entity[ID], M[+B]]
  extends DomainEventPublisher[EntityEvent[ID, E], M, E]
