package etude.foundation.calendar.holiday.domain

import java.time.LocalDate
import java.util.Locale

trait NationalHoliday extends Holiday {
  def title(locale: Locale): String
}

object NationalHoliday {
  def apply(date: LocalDate, title: String): NationalHoliday =
    NationalHolidayImpl(date, title)

  def unapply(holiday: NationalHoliday): Option[LocalDate] =
    Some(holiday.date)
}

private[domain]
case class NationalHolidayImpl(date: LocalDate,
                               title: String)
  extends NationalHoliday {

  def title(locale: Locale): String = title
}
