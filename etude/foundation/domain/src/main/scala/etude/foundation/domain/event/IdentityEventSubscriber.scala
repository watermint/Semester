package etude.foundation.domain.event

import scala.language.higherKinds
import etude.foundation.domain.model.Identity

trait IdentityEventSubscriber[ID <: Identity[_], M[+B]]
  extends DomainEventSubscriber[IdentityEvent[ID], M, ID]
