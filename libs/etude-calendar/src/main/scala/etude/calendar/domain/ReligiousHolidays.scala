package etude.calendar.domain

import etude.religion.Religion
import java.util.Locale
import scala.util.Try
import etude.calendar.infrastructure.provider.GoogleCalendarHolidays

case class ReligiousHolidays(religion: Religion,
                             locale: Locale = Locale.getDefault) extends Holidays{
  lazy val googleCalendar = GoogleCalendarHolidays(locale)

  def holidaysWithReason(span: CalendarDateSpan): Try[Seq[Holiday]] = {
    googleCalendar.religiousHolidays(span, religion)
  }
}
