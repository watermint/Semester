package etude.calendar

import etude.calendar.provider.GoogleCalendarHolidays
import etude.region.Country
import java.util.Locale

case class NationalHolidays(country: Country,
                            locale: Locale = Locale.getDefault) extends Holidays {
  lazy val googleCalendar = GoogleCalendarHolidays(locale)

  def holidaysWithReason(span: CalendarDateSpan): Either[Exception, Seq[Holiday]] = {
    googleCalendar.nationalHolidays(span, country)
  }
}
