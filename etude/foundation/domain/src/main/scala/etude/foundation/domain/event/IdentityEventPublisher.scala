package etude.foundation.domain.event

import scala.language.higherKinds
import etude.foundation.domain.model.Identity

trait IdentityEventPublisher[ID <: Identity[_], M[+B]]
  extends DomainEventPublisher[IdentityEvent[ID], M, ID]
