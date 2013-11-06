package etude.calendar

import java.time.LocalDate


/**
 *
 */
trait Holidays {
  def holidays(span: CalendarDateSpan): Seq[Holiday] = {
    holidaysWithReason(span) match {
      case Left(l) => throw l
      case Right(r) => r
    }
  }

  def holidaysWithReason(span: CalendarDateSpan): Either[Exception, Seq[Holiday]]

  def between(start: String): HolidayDateContainer = HolidayDateContainer(LocalDate.parse(start), this)

  def between(start: LocalDate): HolidayDateContainer = HolidayDateContainer(start, this)
}
