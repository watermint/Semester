package etude.domain.core.event.mutable

import etude.domain.core.event.IdentityEvent
import etude.domain.core.model.Identity

import scala.language.higherKinds

trait IdentityEventPublisherSupport[ID <: Identity[_], M[+B]]
  extends DomainEventPublisherSupport[IdentityEvent[ID], M, ID]
