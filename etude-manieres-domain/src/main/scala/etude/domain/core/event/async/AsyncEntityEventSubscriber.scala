package etude.domain.core.event.async

import etude.domain.core.event.EntityEventSubscriber
import etude.domain.core.model.{Entity, Identity}

import scala.concurrent.Future

trait AsyncEntityEventSubscriber[ID <: Identity[_], E <: Entity[ID]]
  extends EntityEventSubscriber[ID, E, Future]
