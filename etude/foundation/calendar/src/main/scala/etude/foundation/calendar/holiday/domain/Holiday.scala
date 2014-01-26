package etude.foundation.calendar.holiday.domain

import java.time.LocalDate

trait Holiday {
  val date: LocalDate
}

object Holiday {
  def apply(date: LocalDate): Holiday = HolidayImpl(date)

  def unapply(holiday: Holiday): Option[LocalDate] = Some(holiday.date)
}

private[domain]
case class HolidayImpl(date: LocalDate)
  extends Holiday