package etude.commons.domain.calendar

import java.time.LocalDate
import scala.util.{Try, Failure, Success}

trait Holidays {
  def holidays(span: CalendarDateSpan): Seq[Holiday] = {
    holidaysWithReason(span) match {
      case Failure(l) => throw l
      case Success(r) => r
    }
  }

  def holidaysWithReason(span: CalendarDateSpan): Try[Seq[Holiday]]

  def between(start: String): HolidayDateContainer = HolidayDateContainer(LocalDate.parse(start), this)

  def between(start: LocalDate): HolidayDateContainer = HolidayDateContainer(start, this)
}
