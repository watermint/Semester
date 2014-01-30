package etude.foundation.calendar.holiday.infrastructure

import java.time.{Instant, Duration}
import etude.foundation.calendar.holiday.domain.{Calendar, CalendarId, AsyncCalendarRepository}
import etude.foundation.domain.lifecycle.EntityIOContext
import scala.concurrent.{future, Future}
import scala.collection.mutable
import scala.util.Success

case class AsyncCachedCalendarRepository(cacheTimeout: Duration = Duration.ofDays(14),
                                         delegate: AsyncCalendarRepository = AsyncFacadeCalendarRepository())
  extends AsyncCalendarRepository {

  type This <: AsyncCachedCalendarRepository

  protected val resolveCache = mutable.HashMap[CalendarId, (Instant, Calendar)]()

  protected val containsCache = mutable.HashMap[CalendarId, (Instant, Boolean)]()

  def containsByIdentity(identity: CalendarId)(implicit context: EntityIOContext[Future]): Future[Boolean] = {
    implicit val executionContext = getExecutionContext(context)

    containsCache.get(identity) match {
      case Some(Pair(t, r)) =>
        if (t.isAfter(Instant.now())) {
          future(r)
        } else {
          delegate.containsByIdentity(identity) andThen {
            case Success(c) =>
              containsCache.put(identity, Pair(Instant.now.plus(cacheTimeout), c))
          }
        }
      case _ =>
        delegate.containsByIdentity(identity) andThen {
          case Success(c) =>
            containsCache.put(identity, Pair(Instant.now.plus(cacheTimeout), c))
        }
    }
  }

  def resolve(identity: CalendarId)(implicit context: EntityIOContext[Future]): Future[Calendar] = {
    implicit val executionContext = getExecutionContext(context)

    resolveCache.get(identity) match {
      case Some(Pair(t, r)) =>
        if (t.isAfter(Instant.now())) {
          future(r)
        } else {
          delegate.resolve(identity) andThen {
            case Success(e) =>
              resolveCache.put(identity, Pair(Instant.now.plus(cacheTimeout), e))
          }
        }
      case _ =>
        delegate.resolve(identity) andThen {
          case Success(e) =>
            resolveCache.put(identity, Pair(Instant.now.plus(cacheTimeout), e))
        }
    }
  }
}
