package etude.foundation.calendar.holiday.domain

import scala.concurrent.Future
import etude.foundation.domain.lifecycle.async.AsyncEntityReader

trait AsyncNationalHolidaysRepository
  extends NationalHolidaysRepository[Future]
  with AsyncEntityReader[NationalHolidaysId, NationalHolidays]
