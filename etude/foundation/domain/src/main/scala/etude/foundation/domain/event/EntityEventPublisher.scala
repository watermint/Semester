package etude.foundation.domain.event

import etude.foundation.domain.model.{Entity, Identity}

trait EntityEventPublisher[ID <: Identity[_], E <: Entity[ID], M[+B]]
  extends DomainEventPublisher[EntityIOEvent[ID, E], M, E]
