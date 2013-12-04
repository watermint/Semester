package etude.calendar.domain

import java.time.LocalDate
import scala.Some
import scala.util.{Failure, Success}

case class BusinessDayContainer(date: LocalDate, businessDays: BusinessDays) extends CalendarDate {
  def and(end: LocalDate): Seq[BusinessDay] = {
    val span = CalendarDateSpan(date, end)
    BusinessHolidays(businessDays.patterns).holidaysWithReason(span) match {
      case Failure(l) => throw l
      case Success(r) =>
        val days = r.map(_.date).distinct
        span.days.flatMap {
          day =>
            if (days.contains(day)) {
              None
            } else {
              Some(BusinessDay(day))
            }
        }
    }
  }
}
