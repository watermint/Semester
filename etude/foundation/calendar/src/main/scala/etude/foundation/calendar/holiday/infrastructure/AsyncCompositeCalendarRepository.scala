package etude.foundation.calendar.holiday.infrastructure

import etude.foundation.calendar.holiday.domain._
import etude.foundation.domain.lifecycle.EntityIOContext
import scala.concurrent._
import etude.foundation.calendar.holiday.infrastructure.google.AsyncGoogleCalendarRepository
import etude.foundation.calendar.holiday.domain.CalendarId
import etude.foundation.calendar.CalendarNotFoundException
import java.time.{MonthDay, DayOfWeek}
import etude.foundation.i18n.region.iso.Country

case class AsyncCompositeCalendarRepository(dayOfWeekRepository: AsyncCalendarRepository = AsyncRuleBasedCalendarRepository(),
                                            monthDayRepository: AsyncCalendarRepository = AsyncRuleBasedCalendarRepository(),
                                            nationalHolidayRepository: AsyncCalendarRepository = AsyncGoogleCalendarRepository())
  extends AsyncCalendarRepository {

  type This <: AsyncCompositeCalendarRepository

  def containsByIdentity(identity: CalendarId)(implicit context: EntityIOContext[Future]): Future[Boolean] = {
    implicit val executionContext = getExecutionContext(context)

    future {
      identity.name match {
        case n: CompositeCalendarName => true
        case _ => false
      }
    }
  }

  def dayOfWeekHoliday(dayOfWeek: Seq[DayOfWeek], year: Int)
                      (implicit context: EntityIOContext[Future]): Future[Seq[Holiday]] = {
    implicit val executionContext = getExecutionContext(context)
    dayOfWeekRepository.resolve(new CalendarId(DayOfWeekCalendarName(dayOfWeek), year)) map {
      _.holidays
    }
  }

  def monthDayHoliday(monthDay: Seq[MonthDay], year: Int)
                     (implicit context: EntityIOContext[Future]): Future[Seq[Holiday]] = {
    implicit val executionContext = getExecutionContext(context)
    monthDayRepository.resolve(new CalendarId(MonthDayCalendarName(monthDay), year)) map {
      _.holidays
    }
  }

  def nationalHoliday(country: Country, year: Int)
                     (implicit context: EntityIOContext[Future]): Future[Seq[Holiday]] = {
    implicit val executionContext = getExecutionContext(context)
    nationalHolidayRepository.resolve(new CalendarId(NationalHolidayCalendarName(country), year)) map {
      _.holidays
    }
  }

  def holidays(composite: CompositeCalendarName, year: Int)
              (implicit context: EntityIOContext[Future]): Future[Seq[Holiday]] = {
    implicit val executionContext = getExecutionContext(context)

    dayOfWeekHoliday(composite.dayOfWeek, year) zip
      monthDayHoliday(composite.monthDay, year) zip
      nationalHoliday(composite.country, year) map {
      h =>
        h._1._1 ++ h._1._2 ++ h._2
    }
  }

  def resolve(identity: CalendarId)(implicit context: EntityIOContext[Future]): Future[Calendar] = {
    implicit val executionContext = getExecutionContext(context)

    identity.name match {
      case n: CompositeCalendarName =>
        holidays(n, identity.year) map {
          h =>
            new Calendar(identity, h)
        }
      case _ => throw CalendarNotFoundException(s"Calendar not found for ${identity.name}")
    }
  }
}
