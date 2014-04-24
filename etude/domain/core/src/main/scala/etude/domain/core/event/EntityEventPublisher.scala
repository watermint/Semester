package etude.domain.core.event

import scala.language.higherKinds
import etude.domain.core.model.{Entity, Identity}

trait EntityEventPublisher[ID <: Identity[_], E <: Entity[ID], M[+B]]
  extends DomainEventPublisher[EntityEvent[ID, E], M, E]
