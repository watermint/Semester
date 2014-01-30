package etude.app.holiday.domain

import scala.util.Try
import etude.foundation.domain.lifecycle.sync.SyncEntityReader

trait SyncBusinessCalendarRepository
  extends BusinessCalendarRepository[Try]
  with SyncEntityReader[BusinessCalendarId, BusinessCalendar]

