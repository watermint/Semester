package etude.manieres.domain.event.async

import etude.manieres.domain.event.IdentityEventSubscriber
import etude.manieres.domain.model.Identity

import scala.concurrent.Future

trait AsyncIdentityEventSubscriber[ID <: Identity[_]]
  extends IdentityEventSubscriber[ID, Future]
