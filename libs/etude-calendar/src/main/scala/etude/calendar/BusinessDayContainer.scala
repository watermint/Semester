package etude.calendar

import java.time.LocalDate

/**
 *
 */
case class BusinessDayContainer(date: LocalDate, businessDays: BusinessDays) extends CalendarDate {
  def and(end: LocalDate): Seq[BusinessDay] = {
    val span = CalendarDateSpan(date, end)
    BusinessHolidays(businessDays.patterns).holidaysWithReason(span) match {
      case Left(l) => throw l
      case Right(r) =>
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
