package etude.foundation.domain.event.mutable

import scala.language.higherKinds
import etude.foundation.domain.model.{Entity, Identity}
import etude.foundation.domain.event.EntityEvent

trait EntityEventPublisherSupport[ID <: Identity[_], E <: Entity[ID], M[+B]]
  extends DomainEventPublisherSupport[EntityEvent[ID, E], M, E]
