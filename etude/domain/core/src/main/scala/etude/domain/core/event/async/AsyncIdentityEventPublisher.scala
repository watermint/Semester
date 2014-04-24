package etude.domain.core.event.async

import etude.domain.core.model.Identity
import etude.domain.core.event.IdentityEventPublisher
import scala.concurrent.Future

trait AsyncIdentityEventPublisher[ID <: Identity[_]]
  extends IdentityEventPublisher[ID, Future]
