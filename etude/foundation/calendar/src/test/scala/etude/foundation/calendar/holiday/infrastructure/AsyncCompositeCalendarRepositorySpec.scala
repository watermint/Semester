package etude.foundation.calendar.holiday.infrastructure

import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mutable.Specification
import java.util.concurrent.{Executors, ExecutorService}
import scala.concurrent.ExecutionContext
import etude.foundation.domain.lifecycle.async.AsyncEntityIOContext
import scala.concurrent.duration._
import etude.foundation.calendar.holiday.domain.{CalendarId, CompositeCalendarName}
import etude.foundation.i18n.region.iso.Country
import java.time.{DayOfWeek, Month, MonthDay}

@RunWith(classOf[JUnitRunner])
class AsyncCompositeCalendarRepositorySpec extends Specification {
  val executorsPool: ExecutorService = Executors.newCachedThreadPool()

  implicit val executors = ExecutionContext.fromExecutorService(executorsPool)

  implicit val entityIOContext = AsyncEntityIOContext()

  val timeout = Duration(30, SECONDS)

  "Composite calendar repository" should {
    "Only returns JP Holidays" in {
      val name = CompositeCalendarName(Country.JAPAN, Seq(), Seq())
      val cr = AsyncCompositeCalendarRepository()
      val cal = cr.resolve(CalendarId(name, 2014))

      cal map { _.holidays.size } must be_==(17).await(timeout = timeout)
    }

    "returns JP Holidays + specific day" in {
      val name = CompositeCalendarName(Country.JAPAN, Seq(), Seq(MonthDay.of(Month.DECEMBER, 31)))
      val cr = AsyncCompositeCalendarRepository()
      val cal = cr.resolve(CalendarId(name, 2014))

      cal map { _.holidays.size } must be_==(18).await(timeout = timeout)
    }

    "returns JP Holidays + Sundays" in {
      val name = CompositeCalendarName(Country.JAPAN, Seq(DayOfWeek.SUNDAY), Seq())
      val cr = AsyncCompositeCalendarRepository()
      val cal = cr.resolve(CalendarId(name, 2014))

      cal map { _.holidays.size } must be_==(69).await(timeout = timeout)
    }

    "returns JP Holidays + Sundays + specific day" in {
      val name = CompositeCalendarName(Country.JAPAN, Seq(DayOfWeek.SUNDAY), Seq(MonthDay.of(Month.DECEMBER, 31)))
      val cr = AsyncCompositeCalendarRepository()
      val cal = cr.resolve(CalendarId(name, 2014))

      cal map { _.holidays.size } must be_==(70).await(timeout = timeout)
    }
  }
}
