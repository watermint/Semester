package etude.domain.core.event.async

import etude.domain.core.event.EntityEventPublisher
import etude.domain.core.model.{Entity, Identity}

import scala.concurrent.Future

trait AsyncEntityEventPublisher[ID <: Identity[_], E <: Entity[ID]]
  extends EntityEventPublisher[ID, E, Future]
