package etude.domain.core.event

import scala.language.higherKinds
import etude.domain.core.model.Identity

trait IdentityEventPublisher[ID <: Identity[_], M[+B]]
  extends DomainEventPublisher[IdentityEvent[ID], M, ID]
