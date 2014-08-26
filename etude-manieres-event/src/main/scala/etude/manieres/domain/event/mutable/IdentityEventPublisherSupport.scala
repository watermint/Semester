package etude.manieres.domain.event.mutable

import etude.manieres.domain.event.IdentityEvent
import etude.manieres.domain.model.Identity

import scala.language.higherKinds

trait IdentityEventPublisherSupport[ID <: Identity[_], M[+B]]
  extends DomainEventPublisherSupport[IdentityEvent[ID], M, ID]
