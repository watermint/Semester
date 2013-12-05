package etude.commons.domain.calendar

import java.util.Locale
import scala.util.Try
import etude.commons.domain.region.Country
import etude.commons.infrastructure.calendar.provider.GoogleCalendarHolidays

case class NationalHolidays(country: Country,
                            locale: Locale = Locale.getDefault) extends Holidays {
  lazy val googleCalendar = GoogleCalendarHolidays(locale)

  def holidaysWithReason(span: CalendarDateSpan): Try[Seq[Holiday]] = {
    googleCalendar.nationalHolidays(span, country)
  }
}
