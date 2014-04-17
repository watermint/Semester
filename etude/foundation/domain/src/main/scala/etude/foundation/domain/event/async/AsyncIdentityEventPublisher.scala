package etude.foundation.domain.event.async

import etude.foundation.domain.model.Identity
import etude.foundation.domain.event.IdentityEventPublisher
import scala.concurrent.Future

trait AsyncIdentityEventPublisher[ID <: Identity[_]]
  extends IdentityEventPublisher[ID, Future]
