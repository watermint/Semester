package etude.calendar.domain

import etude.region.Country
import java.util.Locale
import scala.util.Try
import etude.calendar.infrastructure.provider.GoogleCalendarHolidays

case class NationalHolidays(country: Country,
                            locale: Locale = Locale.getDefault) extends Holidays {
  lazy val googleCalendar = GoogleCalendarHolidays(locale)

  def holidaysWithReason(span: CalendarDateSpan): Try[Seq[Holiday]] = {
    googleCalendar.nationalHolidays(span, country)
  }
}
