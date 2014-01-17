package etude.foundation.calendar.holiday.infrastructure.google

import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mutable.Specification
import java.util.concurrent.{Executors, ExecutorService}
import scala.concurrent.{Await, Future, ExecutionContext}
import etude.foundation.domain.lifecycle.async.AsyncEntityIOContext
import etude.foundation.i18n.region.iso.Country
import scala.concurrent.duration._
import etude.foundation.calendar.holiday.domain.NationalHolidaysId

@RunWith(classOf[JUnitRunner])
class AsyncGoogleNationalHolidaysRepositorySpec extends Specification {
  val executorsPool: ExecutorService = Executors.newCachedThreadPool()

  implicit val executors = ExecutionContext.fromExecutorService(executorsPool)

  implicit val entityIOContext = AsyncEntityIOContext()

  def result[T](f: Future[T]): T = {
    Await.result(f, Duration(30, SECONDS))
  }

  "Google Calendar" should {
    "URL" in {
      val g = AsyncGoogleNationalHolidaysRepository()
      val jp = Country("JP")
      val year = 2014
      val calendarName = g.supportedCountries.get(jp).get
      val uri = g.calendarUri(calendarName, year)

      uri.toString.startsWith("http://") must beTrue
    }

    "Japanese national holidays" in {
      val g = AsyncGoogleNationalHolidaysRepository()
      val jp = Country("JP")
      val year = 2014

      val jp2014 = result(g.resolve(NationalHolidaysId(jp, year)))
      jp2014.holidays.size must equalTo(17)
    }
  }
}
