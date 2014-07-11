package etude.domain.core.event

import etude.domain.core.model.Identity

class IdentityEvent[ID <: Identity[_]]
(val identity: ID,
 val eventType: IdentityEventType.Value)
  extends DomainEvent[ID]
