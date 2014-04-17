package etude.foundation.domain.event.mutable

import scala.language.higherKinds
import etude.foundation.domain.model.Identity
import etude.foundation.domain.event.IdentityEvent

trait IdentityEventPublisherSupport[ID <: Identity[_], M[+B]]
  extends DomainEventPublisherSupport[IdentityEvent[ID], M, ID]
