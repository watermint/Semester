package etude.foundation.holiday.domain

import java.time.LocalDate
import java.util.Locale

trait Holiday {
  val holidayType: HolidayType.Value

  val date: LocalDate

  def title(locale: Locale): String
}
