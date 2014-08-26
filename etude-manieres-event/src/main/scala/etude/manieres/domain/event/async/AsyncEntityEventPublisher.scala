package etude.manieres.domain.event.async

import etude.manieres.domain.event.EntityEventPublisher
import etude.manieres.domain.model.{Entity, Identity}

import scala.concurrent.Future

trait AsyncEntityEventPublisher[ID <: Identity[_], E <: Entity[ID]]
  extends EntityEventPublisher[ID, E, Future]
