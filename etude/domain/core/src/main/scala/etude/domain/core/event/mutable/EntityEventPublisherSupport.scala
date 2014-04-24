package etude.domain.core.event.mutable

import scala.language.higherKinds
import etude.domain.core.model.{Entity, Identity}
import etude.domain.core.event.EntityEvent

trait EntityEventPublisherSupport[ID <: Identity[_], E <: Entity[ID], M[+B]]
  extends DomainEventPublisherSupport[EntityEvent[ID, E], M, E]
