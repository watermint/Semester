package etude.foundation.calendar.holiday.domain

import etude.foundation.domain.model.Identity
import etude.foundation.i18n.region.iso.Country

case class NationalHolidaysId(country: Country,
                              year: Int)
  extends Identity[(Country, Int)] {

  def value: (Country, Int) = (country, year)
}
