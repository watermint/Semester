package etude.calendar.domain

import java.time.LocalDate

case class ReligiousHoliday(date: LocalDate, title: Option[String]) extends Holiday