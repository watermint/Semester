package etude.domain.core.lifecycle.async

import etude.domain.core.lifecycle.Factory
import scala.concurrent.Future

trait AsyncFactory
  extends Factory
  with AsyncEntityIO
