package etude.domain.core.event.mutable

import scala.language.higherKinds
import etude.domain.core.model.Identity
import etude.domain.core.event.IdentityEvent

trait IdentityEventPublisherSupport[ID <: Identity[_], M[+B]]
  extends DomainEventPublisherSupport[IdentityEvent[ID], M, ID]
