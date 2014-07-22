package etude.manieres.domain.event.async

import etude.manieres.domain.event.EntityEventSubscriber
import etude.manieres.domain.model.{Entity, Identity}

import scala.concurrent.Future

trait AsyncEntityEventSubscriber[ID <: Identity[_], E <: Entity[ID]]
  extends EntityEventSubscriber[ID, E, Future]
