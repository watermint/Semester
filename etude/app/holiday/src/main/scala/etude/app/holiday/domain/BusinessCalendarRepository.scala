package etude.app.holiday.domain

import scala.language.higherKinds

trait BusinessCalendarRepository[M[+A]]
  extends BusinessCalendarReader[M]
