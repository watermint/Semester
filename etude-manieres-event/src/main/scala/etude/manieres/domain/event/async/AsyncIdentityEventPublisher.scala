package etude.manieres.domain.event.async

import etude.manieres.domain.event.IdentityEventPublisher
import etude.manieres.domain.model.Identity

import scala.concurrent.Future

trait AsyncIdentityEventPublisher[ID <: Identity[_]]
  extends IdentityEventPublisher[ID, Future]
