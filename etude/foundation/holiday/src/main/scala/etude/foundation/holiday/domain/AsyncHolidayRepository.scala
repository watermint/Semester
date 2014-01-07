package etude.foundation.holiday.domain

import scala.concurrent.Future
import etude.foundation.domain.lifecycle.async.AsyncEntityReader

trait AsyncHolidayRepository
  extends HolidayRepository[Future]
  with AsyncEntityReader[HolidayId, Holiday]
