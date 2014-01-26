package etude.foundation.calendar.holiday.domain

import scala.language.higherKinds

trait CalendarRepository[M[+A]]
  extends CalendarReader[M]
