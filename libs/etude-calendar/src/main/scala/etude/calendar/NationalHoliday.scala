package etude.calendar

import java.time.LocalDate
import etude.region.Country

case class NationalHoliday(date: LocalDate, title: Option[String], country: Country) extends Holiday
