package etude.domain.core.event

import etude.domain.core.model.Identity

import scala.language.higherKinds

trait IdentityEventSubscriber[ID <: Identity[_], M[+B]]
  extends DomainEventSubscriber[IdentityEvent[ID], M, ID]
