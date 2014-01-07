package etude.foundation.holiday.domain

import scala.language.higherKinds
import etude.foundation.domain.lifecycle.EntityReader

trait HolidayReader[M[+A]]
  extends EntityReader[HolidayId, Holiday, M]
