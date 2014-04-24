package etude.domain.core.event

import scala.language.higherKinds
import etude.domain.core.model.{Entity, Identity}

trait EntityEventSubscriber[ID <: Identity[_], E <: Entity[ID], M[+B]]
  extends DomainEventSubscriber[EntityEvent[ID, E], M, E]
