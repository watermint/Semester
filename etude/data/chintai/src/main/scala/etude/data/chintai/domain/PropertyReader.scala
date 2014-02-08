package etude.data.chintai.domain

import scala.language.higherKinds
import etude.foundation.domain.lifecycle.EntityReader

private[domain]
trait PropertyReader[M[+A]]
  extends EntityReader[PropertyId, Property, M]
