package etude.domain.core.event

import etude.domain.core.model.{Identity, Entity}

class EntityEvent[ID <: Identity[_], E <: Entity[ID]]
(val identity: ID,
 val entity: E,
 val eventType: EntityEventType.Value)
  extends DomainEvent[ID]
