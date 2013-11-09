package etude.calendar

import etude.calendar.provider.GoogleCalendarHolidays
import etude.religion.Religion
import java.util.Locale

case class ReligiousHolidays(religion: Religion,
                             locale: Locale = Locale.getDefault) extends Holidays{
  lazy val googleCalendar = GoogleCalendarHolidays(locale)

  def holidaysWithReason(span: CalendarDateSpan): Either[Exception, Seq[Holiday]] = {
    googleCalendar.religiousHolidays(span, religion)
  }
}
