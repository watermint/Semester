package etude.foundation.calendar.holiday.infrastructure

import etude.foundation.calendar.holiday.domain._
import etude.foundation.domain.lifecycle.EntityIOContext
import scala.concurrent.{future, Future}
import etude.foundation.calendar.holiday.domain.DayOfWeekCalendarName
import etude.foundation.calendar.holiday.domain.CalendarId
import etude.foundation.calendar.CalendarNotFoundException
import java.time.{MonthDay, Month, LocalDate, DayOfWeek}

case class AsyncRuleBasedCalendarRepository()
  extends AsyncCalendarRepository {
  type This <: AsyncRuleBasedCalendarRepository

  def containsByIdentity(identity: CalendarId)(implicit context: EntityIOContext[Future]): Future[Boolean] = {
    implicit val executionContext = getExecutionContext(context)

    future {
      identity.name match {
        case n: DayOfWeekCalendarName => true
        case n: MonthDayCalendarName => true
        case _ => false
      }
    }
  }

  def dayOfWeekDays(dayOfWeek: Seq[DayOfWeek], year: Int): Seq[LocalDate] = {
    val yearDays = Stream.iterate(LocalDate.of(year, Month.JANUARY, 1))(_.plusDays(1))
      .takeWhile(_.isBefore(LocalDate.of(year + 1, Month.JANUARY, 1)))

    yearDays flatMap {
      day =>
        dayOfWeek.exists(_.equals(day.getDayOfWeek)) match {
          case true => Some(day)
          case _ => None
        }
    }
  }

  def monthDayDays(monthDay: Seq[MonthDay], year: Int): Seq[LocalDate] = {
    monthDay.map(_.atYear(year))
  }

  def asHoliday(days: Seq[LocalDate]): Seq[Holiday] = {
    days.map {
      d =>
        Holiday(d)
    }
  }

  def resolve(identity: CalendarId)(implicit context: EntityIOContext[Future]): Future[Calendar] = {
    implicit val executionContext = getExecutionContext(context)

    future {
      identity.name match {
        case n: DayOfWeekCalendarName =>
          new Calendar(identity, asHoliday(dayOfWeekDays(n.dayOfWeek, identity.year)))
        case n: MonthDayCalendarName =>
          new Calendar(identity, asHoliday(monthDayDays(n.days, identity.year)))
        case _ =>
          throw CalendarNotFoundException(s"Calendar not found for ${identity.name}")
      }
    }
  }
}
