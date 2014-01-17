package etude.foundation.calendar.holiday.domain

import java.time.LocalDate

case class NationalHoliday(date: LocalDate,
                           title: String)
  extends Holiday