package etude.manieres.domain.event

import etude.manieres.domain.model.Identity

class IdentityEvent[ID <: Identity[_]]
(val identity: ID,
 val eventType: IdentityEventType.Value)
  extends DomainEvent[ID]
