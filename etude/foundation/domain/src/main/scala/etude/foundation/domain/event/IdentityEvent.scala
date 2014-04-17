package etude.foundation.domain.event

import etude.foundation.domain.model.Identity

class IdentityEvent[ID <: Identity[_]]
(val identity: ID,
 val eventType: IdentityEventType.Value)
  extends DomainEvent[ID]
