package etude.foundation.calendar.holiday.domain

import etude.foundation.i18n.region.iso.Country
import java.time.{MonthDay, DayOfWeek}

trait CalendarName

case class NationalHolidayCalendarName(country: Country)
  extends CalendarName

case class DayOfWeekCalendarName(dayOfWeek: Seq[DayOfWeek])
  extends CalendarName

case class MonthDayCalendarName(days: Seq[MonthDay])
  extends CalendarName

case class CompositeCalendarName(country: Country,
                                 dayOfWeek: Seq[DayOfWeek],
                                 monthDay: Seq[MonthDay])
  extends CalendarName

case class NamedCalendarName(name: String)
  extends CalendarName
