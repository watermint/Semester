package etude

import etude.region.Country
import etude.religion.Religion
import java.time.LocalDate
import etude.calendar.domain.{ReligiousHolidays, NationalHolidays, BusinessHolidays, BusinessDays}

package object calendar {
  def businessHolidays: BusinessHolidays = BusinessHolidays()

  def businessDays: BusinessDays = BusinessDays()
  
  implicit def parseAsLocalDate(dateText: String): LocalDate = LocalDate.parse(dateText)

  implicit def religionHolidays(religion: Religion): ReligiousHolidays = ReligiousHolidays(religion)

  implicit def nationalHolidays(country: Country): NationalHolidays = NationalHolidays(country)
}
