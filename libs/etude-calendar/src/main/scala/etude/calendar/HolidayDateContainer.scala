package etude.calendar

import java.time.LocalDate

/**
 *
 */
case class HolidayDateContainer(date: LocalDate, holidays: Holidays) extends CalendarDate {
  def and(other: LocalDate): Seq[Holiday] =
    CalendarDateSpan(date, other).holidays(holidays)
}
