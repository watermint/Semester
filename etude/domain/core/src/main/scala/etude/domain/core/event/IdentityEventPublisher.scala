package etude.domain.core.event

import etude.domain.core.model.Identity

import scala.language.higherKinds

trait IdentityEventPublisher[ID <: Identity[_], M[+B]]
  extends DomainEventPublisher[IdentityEvent[ID], M, ID]
