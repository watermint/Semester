package etude.calendar.domain

import java.time.LocalDate

case class BusinessHoliday(date: LocalDate, title: Option[String] = None) extends Holiday