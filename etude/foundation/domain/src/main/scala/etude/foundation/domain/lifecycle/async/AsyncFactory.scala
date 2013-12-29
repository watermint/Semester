package etude.foundation.domain.lifecycle.async

import etude.foundation.domain.lifecycle.Factory
import scala.concurrent.Future

trait AsyncFactory
  extends Factory
  with AsyncEntityIO
