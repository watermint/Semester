package etude.manieres.domain.event

import etude.manieres.domain.model.Identity

trait IdentityEventSubscriber[ID <: Identity[_], M[+B]]
  extends DomainEventSubscriber[IdentityEvent[ID], M, ID]
