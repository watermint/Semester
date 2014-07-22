package etude.domain.core.event.mutable

import etude.domain.core.event.EntityEvent
import etude.domain.core.model.{Entity, Identity}

import scala.language.higherKinds

trait EntityEventPublisherSupport[ID <: Identity[_], E <: Entity[ID], M[+B]]
  extends DomainEventPublisherSupport[EntityEvent[ID, E], M, E]
