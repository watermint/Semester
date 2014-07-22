package etude.manieres.domain.lifecycle.async

import etude.manieres.domain.lifecycle.Factory

trait AsyncFactory
  extends Factory
  with AsyncEntityIO
