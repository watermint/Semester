package etude.calendar

import java.time.{DayOfWeek, MonthDay, LocalDate}
import etude.region.Country
import etude.religion.Religion
import java.util.Locale
import etude.aggregation.Aggregation

/**
 *
 */
case class BusinessHolidays(patterns: Seq[BusinessHolidayPattern] = Seq(),
                            locale: Locale = Locale.getDefault) extends Holidays {

  def +(day: LocalDate): BusinessHolidays = withSpecificDay(day)

  def +(monthDay: MonthDay): BusinessHolidays = withMonthDay(monthDay)

  def +(dayOfWeek: DayOfWeek): BusinessHolidays = withDayOfWeek(dayOfWeek)

  def +(country: Country): BusinessHolidays = withCountry(country)

  def +(religion: Religion): BusinessHolidays = withReligion(religion)

  def withSpecificDay(day: LocalDate, title: Option[String] = None): BusinessHolidays =
    BusinessHolidays(patterns :+ BusinessHolidayPatternSpecificDay(day, title))

  def withMonthDay(monthDay: MonthDay, title: Option[String] = None): BusinessHolidays =
    BusinessHolidays(patterns :+ BusinessHolidayPatternMonthDay(monthDay, title))

  def withDayOfWeek(dayOfWeek: DayOfWeek, title: Option[String] = None): BusinessHolidays =
    BusinessHolidays(patterns :+ BusinessHolidayPatternDayOfWeek(dayOfWeek, title))

  def withCountry(country: Country): BusinessHolidays =
    BusinessHolidays(patterns :+ BusinessHolidayPatternCountry(country))

  def withReligion(religion: Religion): BusinessHolidays =
    BusinessHolidays(patterns :+ BusinessHolidayPatternReligious(religion))

  def holidaysWithReason(span: CalendarDateSpan): Either[Exception, Seq[Holiday]] = {
    Aggregation.aggregate(patterns.map {
      p =>
        p -> { () => p.holidaysWithReason(span) }
    }.toMap) match {
      case Left(e) => Left(e)
      case Right(r) => Right(r.flatten)
    }
  }
}

object BusinessHolidays {
  def apply(day: LocalDate, title: String): BusinessHolidays =
    BusinessHolidays(Seq(BusinessHolidayPatternSpecificDay(day, Some(title))))

  def apply(day: LocalDate): BusinessHolidays =
    BusinessHolidays(Seq(BusinessHolidayPatternSpecificDay(day)))

  def apply(monthDay: MonthDay, title: String): BusinessHolidays =
    BusinessHolidays(Seq(BusinessHolidayPatternMonthDay(monthDay, Some(title))))

  def apply(monthDay: MonthDay): BusinessHolidays =
    BusinessHolidays(Seq(BusinessHolidayPatternMonthDay(monthDay)))

  def apply(dayOfWeek: DayOfWeek, title: String): BusinessHolidays =
    BusinessHolidays(Seq(BusinessHolidayPatternDayOfWeek(dayOfWeek, Some(title))))

  def apply(dayOfWeek: DayOfWeek): BusinessHolidays =
    BusinessHolidays(Seq(BusinessHolidayPatternDayOfWeek(dayOfWeek)))

  def apply(country: Country): BusinessHolidays =
    BusinessHolidays(Seq(BusinessHolidayPatternCountry(country)))

  def apply(religion: Religion): BusinessHolidays =
    BusinessHolidays(Seq(BusinessHolidayPatternReligious(religion)))
}

trait BusinessHolidayPattern extends Holidays

case class BusinessHolidayPatternSpecificDay(day: LocalDate,
                                             title: Option[String] = None)
  extends BusinessHolidayPattern {

  def holidaysWithReason(span: CalendarDateSpan): Either[Exception, Seq[Holiday]] = {
    if (span.isBetween(day)) {
      Right(Seq(BusinessHoliday(day, title)))
    } else {
      Right(Seq())
    }
  }
}

case class BusinessHolidayPatternMonthDay(monthDay: MonthDay,
                                          title: Option[String] = None)
  extends BusinessHolidayPattern {

  def holidaysWithReason(span: CalendarDateSpan): Either[Exception, Seq[Holiday]] = {
    Right(
      span.years.flatMap {
        y =>
          val d = monthDay.atYear(y)
          if (span.isBetween(d)) {
            Some(BusinessHoliday(d, title))
          } else {
            None
          }
      }
    )
  }
}

case class BusinessHolidayPatternDayOfWeek(dayOfWeek: DayOfWeek,
                                           title: Option[String] = None)
  extends BusinessHolidayPattern {

  def holidaysWithReason(span: CalendarDateSpan): Either[Exception, Seq[Holiday]] = {
    Right(span.weeks(dayOfWeek).map(BusinessHoliday(_, title)))
  }
}

case class BusinessHolidayPatternCountry(country: Country)
  extends BusinessHolidayPattern {
  lazy val nationalHolidays = NationalHolidays(country)

  def holidaysWithReason(span: CalendarDateSpan): Either[Exception, Seq[Holiday]] = nationalHolidays.holidaysWithReason(span)
}

case class BusinessHolidayPatternReligious(religion: Religion)
  extends BusinessHolidayPattern {
  lazy val religiousHolidays = ReligiousHolidays(religion)

  def holidaysWithReason(span: CalendarDateSpan): Either[Exception, Seq[Holiday]] = religiousHolidays.holidaysWithReason(span)
}
