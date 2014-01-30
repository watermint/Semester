package etude.app.holiday.infrastructure

import etude.foundation.calendar.holiday.domain.{Calendar, CalendarId, AsyncCalendarRepository}
import etude.foundation.domain.lifecycle.EntityIOContext
import scala.concurrent.Future
import etude.foundation.calendar.holiday.infrastructure.google.AsyncGoogleCalendarRepository
import etude.foundation.calendar.holiday.infrastructure.AsyncCachedCalendarRepository

class AsyncAppCalendarRepository
  extends AsyncCalendarRepository {

  type This <: AsyncAppCalendarRepository

  protected val delegate = AsyncCachedCalendarRepository()

  def containsByIdentity(identity: CalendarId)
                        (implicit context: EntityIOContext[Future]): Future[Boolean] = {
    delegate.containsByIdentity(identity)
  }

  def resolve(identity: CalendarId)
             (implicit context: EntityIOContext[Future]): Future[Calendar] = {
    delegate.resolve(identity)
  }
}
