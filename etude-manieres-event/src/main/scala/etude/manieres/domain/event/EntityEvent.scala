package etude.manieres.domain.event

import etude.manieres.domain.model.{Entity, Identity}

class EntityEvent[ID <: Identity[_], E <: Entity[ID]]
(val identity: ID,
 val entity: E,
 val eventType: EntityEventType.Value)
  extends DomainEvent[ID]
