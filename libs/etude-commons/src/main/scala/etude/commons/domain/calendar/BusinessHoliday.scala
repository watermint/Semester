package etude.commons.domain.calendar

import java.time.LocalDate

case class BusinessHoliday(date: LocalDate, title: Option[String] = None) extends Holiday