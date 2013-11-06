package etude.calendar

import etude.calendar.provider.GoogleCalendarHolidays
import java.util.Locale
import etude.religion.Religion

/**
 *
 */
case class ReligiousHolidays(religion: Religion,
                             locale: Locale = Locale.getDefault) extends Holidays{
  lazy val googleCalendar = GoogleCalendarHolidays(locale)

  def holidaysWithReason(span: CalendarDateSpan): Either[Exception, Seq[Holiday]] = {
    googleCalendar.religiousHolidays(span, religion)
  }
}
