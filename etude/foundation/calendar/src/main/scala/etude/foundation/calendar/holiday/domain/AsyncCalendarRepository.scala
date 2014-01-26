package etude.foundation.calendar.holiday.domain

import scala.concurrent.Future
import etude.foundation.domain.lifecycle.async.AsyncEntityReader

trait AsyncCalendarRepository
  extends CalendarRepository[Future]
  with AsyncEntityReader[CalendarId, Calendar]
