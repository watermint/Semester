package etude.foundation.calendar.holiday.domain

import scala.language.higherKinds
import etude.foundation.domain.lifecycle.EntityReader

private[domain]
trait CalendarReader[M[+A]]
  extends EntityReader[CalendarId, Calendar, M]