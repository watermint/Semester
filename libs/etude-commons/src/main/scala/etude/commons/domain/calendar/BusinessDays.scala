package etude.commons.domain.calendar

import java.time.{DayOfWeek, MonthDay, LocalDate}
import etude.commons.domain.region.Country

case class BusinessDays(patterns: Seq[BusinessHolidayPattern] = Seq()) {
  def -(day: LocalDate): BusinessDays =
    BusinessDays(patterns :+ BusinessHolidayPatternSpecificDay(day))

  def -(monthDay: MonthDay): BusinessDays =
    BusinessDays(patterns :+ BusinessHolidayPatternMonthDay(monthDay))

  def -(dayOfWeek: DayOfWeek): BusinessDays =
    BusinessDays(patterns :+ BusinessHolidayPatternDayOfWeek(dayOfWeek))

  def -(country: Country): BusinessDays =
    BusinessDays(patterns :+ BusinessHolidayPatternCountry(country))

  def between(start: LocalDate): BusinessDayContainer = BusinessDayContainer(start, this)
}
