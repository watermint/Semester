package etude.app.holiday.infrastructure.named

import etude.foundation.calendar.holiday.domain._
import etude.foundation.domain.lifecycle.EntityIOContext
import scala.concurrent.Future
import etude.foundation.calendar.holiday.domain.CalendarId
import etude.app.holiday.domain.{BusinessCalendar, BusinessCalendarId, SyncBusinessCalendarRepository}
import etude.foundation.domain.lifecycle.sync.SyncEntityIOContext
import scala.util.Success

class AsyncNamedCalendarRepository(protected val delegate: AsyncCalendarRepository,
                                   protected val business: SyncBusinessCalendarRepository)
  extends AsyncCalendarRepository {
  type This <: AsyncNamedCalendarRepository

  protected def perform[T](identity: CalendarId)(d: CalendarId => T)(implicit context: EntityIOContext[Future]): T = {
    identity.name match {
      case n: NamedCalendarName =>
        implicit val context = SyncEntityIOContext
        business.resolve(new BusinessCalendarId(n.name)) match {
          case Success(bc: BusinessCalendar) =>
            d(identity.copy(name = bc.calendarName))
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
