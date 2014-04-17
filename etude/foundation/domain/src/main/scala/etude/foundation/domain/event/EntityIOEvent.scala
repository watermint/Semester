package etude.foundation.domain.event

import etude.foundation.domain.model.{Identity, Entity}

class EntityIOEvent[ID <: Identity[_], E <: Entity[ID]]
(val identity: ID,
 val entity: E,
 val eventType: EntityEventType.Value)
  extends DomainEvent[ID]
