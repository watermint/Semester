package etude.foundation.domain.event.async

import etude.foundation.domain.event.EntityEventPublisher
import etude.foundation.domain.model.{Entity, Identity}
import scala.concurrent.Future

trait AsyncEntityEventPublisher[ID <: Identity[_], E <: Entity[ID]]
  extends EntityEventPublisher[ID, E, Future]
