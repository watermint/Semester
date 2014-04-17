package etude.foundation.domain.event

import scala.language.higherKinds
import etude.foundation.domain.model.{Entity, Identity}

trait EntityEventPublisher[ID <: Identity[_], E <: Entity[ID], M[+B]]
  extends DomainEventPublisher[EntityEvent[ID, E], M, E]
