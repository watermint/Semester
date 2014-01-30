package etude.app.holiday.domain

import scala.language.higherKinds
import etude.foundation.domain.lifecycle.EntityReader

private[domain]
trait BusinessCalendarReader[M[+A]]
  extends EntityReader[BusinessCalendarId, BusinessCalendar, M]

