package etude.domain.core.event

import scala.language.higherKinds
import etude.domain.core.model.Identity

trait IdentityEventSubscriber[ID <: Identity[_], M[+B]]
  extends DomainEventSubscriber[IdentityEvent[ID], M, ID]
