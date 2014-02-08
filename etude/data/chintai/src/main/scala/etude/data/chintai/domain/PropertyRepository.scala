package etude.data.chintai.domain

import scala.language.higherKinds

trait PropertyRepository[M[+A]]
  extends PropertyReader[M]
