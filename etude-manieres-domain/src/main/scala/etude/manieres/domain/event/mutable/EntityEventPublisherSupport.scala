package etude.manieres.domain.event.mutable

import etude.manieres.domain.event.EntityEvent
import etude.manieres.domain.model.{Entity, Identity}

import scala.language.higherKinds

trait EntityEventPublisherSupport[ID <: Identity[_], E <: Entity[ID], M[+B]]
  extends DomainEventPublisherSupport[EntityEvent[ID, E], M, E]
