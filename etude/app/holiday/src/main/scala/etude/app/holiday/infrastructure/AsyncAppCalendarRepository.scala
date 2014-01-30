package etude.app.holiday.infrastructure

import etude.foundation.calendar.holiday.domain.{Calendar, CalendarId, AsyncCalendarRepository}
import etude.foundation.domain.lifecycle.EntityIOContext
import scala.concurrent.Future
import etude.foundation.calendar.holiday.infrastructure.AsyncCachedCalendarRepository
import etude.app.holiday.infrastructure.named.{SyncBusinessCalendarRepositoryOnFile, AsyncNamedCalendarRepository}
import java.io.File

case class AsyncAppCalendarRepository(businessCalendarDefinition: File)
  extends AsyncCalendarRepository {

  type This <: AsyncAppCalendarRepository

  protected val delegate = new AsyncNamedCalendarRepository(
    delegate = AsyncCachedCalendarRepository(),
    business = SyncBusinessCalendarRepositoryOnFile(businessCalendarDefinition))

  def containsByIdentity(identity: CalendarId)
                        (implicit context: EntityIOContext[Future]): Future[Boolean] = {
    delegate.containsByIdentity(identity)
  }

  def resolve(identity: CalendarId)
             (implicit context: EntityIOContext[Future]): Future[Calendar] = {
    delegate.resolve(identity)
  }
}
