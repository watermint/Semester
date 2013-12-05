package etude.commons.domain.calendar

import java.time.LocalDate
import etude.commons.domain.region.Country

case class NationalHoliday(date: LocalDate, title: Option[String], country: Country) extends Holiday
