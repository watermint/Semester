package etude.app.holiday.infrastructure

import etude.foundation.calendar.holiday.domain._
import etude.foundation.domain.lifecycle.EntityIOContext
import scala.concurrent.Future
import etude.foundation.calendar.holiday.domain.CalendarId
import etude.foundation.i18n.region.iso.Country
import java.time.{MonthDay, Month, DayOfWeek}

class AsyncNamedCalendarRepository(protected val delegate: AsyncCalendarRepository)
  extends AsyncCalendarRepository {
  type This <: AsyncNamedCalendarRepository

  protected val names: Map[String, CalendarName] = Map(
    "sample" -> CompositeCalendarName(
      country = Country("JP"),
      dayOfWeek = Seq(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY),
      monthDay = Seq(
        MonthDay.of(Month.JANUARY, 1),
        MonthDay.of(Month.JANUARY, 2),
        MonthDay.of(Month.JANUARY, 3),
        MonthDay.of(Month.DECEMBER, 30),
        MonthDay.of(Month.DECEMBER, 31)
      )
    )
  )

  protected def perform[T](identity: CalendarId)(d: CalendarId => T)(implicit context: EntityIOContext[Future]): T = {
    identity.name match {
      case n: NamedCalendarName =>
        names.get(n.name) match {
          case Some(name) =>
            d(identity.copy(name = name))
          case _ =>
            d(identity)
        }
      case _ =>
        d(identity)
    }
  }

  def containsByIdentity(identity: CalendarId)(implicit context: EntityIOContext[Future]): Future[Boolean] = {
    perform(identity)(delegate.containsByIdentity)
  }

  def resolve(identity: CalendarId)(implicit context: EntityIOContext[Future]): Future[Calendar] = {
    perform(identity)(delegate.resolve)
  }
}
