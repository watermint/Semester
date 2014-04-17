package etude.foundation.domain.event.mutable

import scala.language.higherKinds
import etude.foundation.domain.model.{Entity, Identity}
import etude.foundation.domain.event.EntityIOEvent

trait EntityEventPublisherSupport[ID <: Identity[_], E <: Entity[ID], M[+B]]
  extends DomainEventPublisherSupport[EntityIOEvent[ID, E], M, E]
