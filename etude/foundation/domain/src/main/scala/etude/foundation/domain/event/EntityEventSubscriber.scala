package etude.foundation.domain.event

import scala.language.higherKinds
import etude.foundation.domain.model.{Entity, Identity}

trait EntityEventSubscriber[ID <: Identity[_], E <: Entity[ID], M[+B]]
  extends DomainEventSubscriber[EntityEvent[ID, E], M, E]
