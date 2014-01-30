package etude.app.holiday.application

import com.twitter.finatra.FinatraServer
import etude.app.holiday.application.api.{Named, Country}
import etude.foundation.calendar.holiday.domain.AsyncCalendarRepository
import etude.app.holiday.infrastructure.AsyncAppCalendarRepository

object App extends FinatraServer {
  val calendarRepository: AsyncCalendarRepository = new AsyncAppCalendarRepository()

  register(new Country(calendarRepository))
  register(new Named(calendarRepository))
}
