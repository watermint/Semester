package etude.foundation.calendar.holiday.domain

import etude.foundation.domain.model.Entity

class NationalHolidays(val identity: NationalHolidaysId,
                       val holidays: Seq[NationalHoliday])
  extends Entity[NationalHolidaysId] {

  def copy(holidays: Seq[NationalHoliday] = this.holidays): NationalHolidays = {
    new NationalHolidays(
      identity,
      holidays
    )
  }
}
