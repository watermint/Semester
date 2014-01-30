package etude.app.holiday.domain

import etude.foundation.domain.model.Entity
import etude.foundation.i18n.region.iso.Country
import java.time.{MonthDay, DayOfWeek}
import etude.foundation.calendar.holiday.domain.{CompositeCalendarName, CalendarName}

class BusinessCalendar(val name: BusinessCalendarId,
                       val country: Country,
                       val dayOfWeek: Seq[DayOfWeek],
                       val monthDay: Seq[MonthDay])
  extends Entity[BusinessCalendarId] {

  val identity: BusinessCalendarId = name

  val calendarName: CalendarName = CompositeCalendarName(
    country = country,
    dayOfWeek = dayOfWeek,
    monthDay = monthDay
  )
}
