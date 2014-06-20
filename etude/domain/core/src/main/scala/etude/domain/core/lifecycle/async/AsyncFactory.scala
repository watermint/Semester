package etude.domain.core.lifecycle.async

import etude.domain.core.lifecycle.Factory

trait AsyncFactory
  extends Factory
  with AsyncEntityIO
