package etude.foundation.holiday.domain

import etude.foundation.domain.model.Entity
import java.time.LocalDate

class Holiday(val holidayId: HolidayId,
              val holidayType: HolidayType.Value,
              val title: String,
              val date: LocalDate)
  extends Entity[HolidayId] {

  val identity: HolidayId = holidayId
}
