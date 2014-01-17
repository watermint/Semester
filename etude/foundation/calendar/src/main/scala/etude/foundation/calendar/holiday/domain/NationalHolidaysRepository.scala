package etude.foundation.calendar.holiday.domain

import scala.language.higherKinds

trait NationalHolidaysRepository[M[+A]]
  extends NationalHolidaysReader[M]
