package etude.foundation.calendar.holiday.domain

import etude.foundation.domain.model.Entity
import java.time.LocalDate

class Calendar(val identity: CalendarId,
               val holidays: Seq[Holiday])
  extends Entity[CalendarId] {

  def copy(holidays: Seq[Holiday] = this.holidays): Calendar = {
    new Calendar(
      identity,
      holidays
    )
  }

  def isHoliday(date: LocalDate): Boolean = {
    holidays.exists(_.date.equals(date))
  }
}
