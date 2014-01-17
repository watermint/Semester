package etude.foundation.calendar.holiday.domain

import java.time.LocalDate

trait Holiday {
  val date: LocalDate

  val title: String
}
