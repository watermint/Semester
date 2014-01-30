package etude.app.holiday.application

import com.twitter.finatra.FinatraServer
import etude.app.holiday.application.api.{Business, Country}
import etude.foundation.calendar.holiday.domain.AsyncCalendarRepository
import etude.app.holiday.infrastructure.AsyncAppCalendarRepository
import java.io.File

class App(businessCalendarDefinition: File)
  extends FinatraServer {

  val calendarRepository: AsyncCalendarRepository =
    new AsyncAppCalendarRepository(businessCalendarDefinition)

  register(new Country(calendarRepository))
  register(new Business(calendarRepository))
}

object DefaultApp
  extends App(
    new File(System.getProperty("user.home"), ".etude-holiday/calendars.json"))
