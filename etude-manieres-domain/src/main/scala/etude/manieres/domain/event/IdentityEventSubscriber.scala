package etude.manieres.domain.event

import etude.manieres.domain.model.Identity

import scala.language.higherKinds

trait IdentityEventSubscriber[ID <: Identity[_], M[+B]]
  extends DomainEventSubscriber[IdentityEvent[ID], M, ID]
