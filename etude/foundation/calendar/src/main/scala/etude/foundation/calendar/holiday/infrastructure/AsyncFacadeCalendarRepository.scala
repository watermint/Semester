package etude.foundation.calendar.holiday.infrastructure

import etude.foundation.calendar.holiday.domain._
import etude.foundation.domain.lifecycle.EntityIOContext
import scala.concurrent.Future
import etude.foundation.calendar.holiday.domain.CompositeCalendarName
import etude.foundation.calendar.holiday.infrastructure.google.AsyncGoogleCalendarRepository
import etude.foundation.calendar.holiday.domain.CalendarId
import etude.foundation.calendar.CalendarNotFoundException

case class AsyncFacadeCalendarRepository()
  extends AsyncCalendarRepository {
  type This <: AsyncFacadeCalendarRepository

  protected val composite: AsyncCalendarRepository = AsyncCompositeCalendarRepository()

  protected val ruleBased: AsyncCalendarRepository = AsyncRuleBasedCalendarRepository()

  protected val nationalHoliday: AsyncCalendarRepository = AsyncGoogleCalendarRepository()

  def containsByIdentity(identity: CalendarId)(implicit context: EntityIOContext[Future]): Future[Boolean] = {
    identity.name match {
      case _: CompositeCalendarName => composite.containsByIdentity(identity)
      case _: DayOfWeekCalendarName => ruleBased.containsByIdentity(identity)
      case _: MonthDayCalendarName => ruleBased.containsByIdentity(identity)
      case _: NationalHolidayCalendarName => nationalHoliday.containsByIdentity(identity)
      case _ => throw CalendarNotFoundException(s"Calendar not found for ${identity.name}")
    }
  }

  def resolve(identity: CalendarId)(implicit context: EntityIOContext[Future]): Future[Calendar] = {
    identity.name match {
      case _: CompositeCalendarName => composite.resolve(identity)
      case _: DayOfWeekCalendarName => ruleBased.resolve(identity)
      case _: MonthDayCalendarName => ruleBased.resolve(identity)
      case _: NationalHolidayCalendarName => nationalHoliday.resolve(identity)
      case _ => throw CalendarNotFoundException(s"Calendar not found for ${identity.name}")
    }
  }
}
