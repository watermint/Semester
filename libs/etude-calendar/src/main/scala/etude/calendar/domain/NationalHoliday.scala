package etude.calendar.domain

import etude.region.Country
import java.time.LocalDate

case class NationalHoliday(date: LocalDate, title: Option[String], country: Country) extends Holiday
