package etude.foundation.domain.event

import etude.foundation.domain.model.{Entity, Identity}

trait EntityEventSubscriber[ID <: Identity[_], E <: Entity[ID], M[+B]]
  extends DomainEventSubscriber[EntityIOEvent[ID, E], M, E]
