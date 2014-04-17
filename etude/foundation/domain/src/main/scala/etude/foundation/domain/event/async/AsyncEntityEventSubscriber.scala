package etude.foundation.domain.event.async

import etude.foundation.domain.event.EntityEventSubscriber
import etude.foundation.domain.model.{Entity, Identity}
import scala.concurrent.Future

trait AsyncEntityEventSubscriber[ID <: Identity[_], E <: Entity[ID]]
  extends EntityEventSubscriber[ID, E, Future]
