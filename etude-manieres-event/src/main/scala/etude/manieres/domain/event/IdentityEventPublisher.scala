package etude.manieres.domain.event

import etude.manieres.domain.model.Identity

trait IdentityEventPublisher[ID <: Identity[_], M[+B]]
  extends DomainEventPublisher[IdentityEvent[ID], M, ID]
