package etude.foundation.domain.event.async

import etude.foundation.domain.model.Identity
import etude.foundation.domain.event.IdentityEventSubscriber
import scala.concurrent.Future

trait AsyncIdentityEventSubscriber[ID <: Identity[_]]
  extends IdentityEventSubscriber[ID, Future]
