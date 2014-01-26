package etude.foundation.calendar.holiday.infrastructure.google

import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mutable.Specification
import java.util.concurrent.{Executors, ExecutorService}
import scala.concurrent.{Await, Future, ExecutionContext}
import etude.foundation.domain.lifecycle.async.AsyncEntityIOContext
import etude.foundation.i18n.region.iso.Country
import scala.concurrent.duration._
import etude.foundation.calendar.holiday.domain.{Calendar, NationalHolidayCalendarName, CalendarId}

@RunWith(classOf[JUnitRunner])
class AsyncGoogleCalendarRepositorySpec extends Specification {
  val executorsPool: ExecutorService = Executors.newCachedThreadPool()

  implicit val executors = ExecutionContext.fromExecutorService(executorsPool)

  implicit val entityIOContext = AsyncEntityIOContext()

  val timeout = Duration(30, SECONDS)

  "Google Calendar" should {
    "URL" in {
      val g = AsyncGoogleCalendarRepository()
      val jp = Country("JP")
      val year = 2014
      val calendarName = g.supportedCountries.get(jp).get
      val uri = g.calendarUri(calendarName, year)

      uri.toString.startsWith("http://") must beTrue
    }

    "Japanese national holidays" in {
      val g = AsyncGoogleCalendarRepository()
      val jp = Country("JP")
      val year = 2014
      val name = NationalHolidayCalendarName(jp)
      val id = CalendarId(name, year)
      val cal: Future[Calendar] = g.resolve(id)

      cal.map { c => c.holidays.size } must be_==(17).await(timeout = timeout)
    }
  }
}
