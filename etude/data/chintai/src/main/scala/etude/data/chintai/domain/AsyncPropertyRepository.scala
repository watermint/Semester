package etude.data.chintai.domain

import scala.concurrent.Future
import etude.foundation.domain.lifecycle.async.AsyncEntityReader

trait AsyncPropertyRepository
  extends PropertyRepository[Future]
  with AsyncEntityReader[PropertyId, Property]
