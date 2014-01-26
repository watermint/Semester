package etude.foundation.calendar.holiday.domain

import etude.foundation.domain.model.Identity
import etude.foundation.i18n.region.iso.Country

case class CalendarId(name: CalendarName,
                      year: Int)
  extends Identity[(CalendarName, Int)] {

  def value: (CalendarName, Int) = (name, year)
}
