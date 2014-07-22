package etude.domain.core.event.async

import etude.domain.core.event.IdentityEventSubscriber
import etude.domain.core.model.Identity

import scala.concurrent.Future

trait AsyncIdentityEventSubscriber[ID <: Identity[_]]
  extends IdentityEventSubscriber[ID, Future]
