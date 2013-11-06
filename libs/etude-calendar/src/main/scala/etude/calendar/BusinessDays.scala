package etude.calendar

import java.time.{DayOfWeek, MonthDay, LocalDate}
import etude.region.Country
import etude.religion.Religion

/**
 *
 */
case class BusinessDays(patterns: Seq[BusinessHolidayPattern] = Seq()) {
  def -(day: LocalDate): BusinessDays =
    BusinessDays(patterns :+ BusinessHolidayPatternSpecificDay(day))

  def -(monthDay: MonthDay): BusinessDays =
    BusinessDays(patterns :+ BusinessHolidayPatternMonthDay(monthDay))

  def -(dayOfWeek: DayOfWeek): BusinessDays =
    BusinessDays(patterns :+ BusinessHolidayPatternDayOfWeek(dayOfWeek))

  def -(country: Country): BusinessDays =
    BusinessDays(patterns :+ BusinessHolidayPatternCountry(country))

  def -(religion: Religion): BusinessDays =
    BusinessDays(patterns :+ BusinessHolidayPatternReligious(religion))

  def between(start: LocalDate): BusinessDayContainer = BusinessDayContainer(start, this)
}
