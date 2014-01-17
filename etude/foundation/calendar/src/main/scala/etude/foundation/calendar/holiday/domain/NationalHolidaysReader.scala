package etude.foundation.calendar.holiday.domain

import scala.language.higherKinds
import etude.foundation.domain.lifecycle.EntityReader

private[domain]
trait NationalHolidaysReader[M[+A]]
  extends EntityReader[NationalHolidaysId, NationalHolidays, M]