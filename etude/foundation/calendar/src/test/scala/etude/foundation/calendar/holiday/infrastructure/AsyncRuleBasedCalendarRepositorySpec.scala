package etude.foundation.calendar.holiday.infrastructure

import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mutable.Specification
import java.util.concurrent.{Executors, ExecutorService}
import scala.concurrent.ExecutionContext
import etude.foundation.domain.lifecycle.async.AsyncEntityIOContext
import scala.concurrent.duration._
import etude.foundation.calendar.holiday.domain.{MonthDayCalendarName, DayOfWeekCalendarName, CalendarId}
import java.time.{MonthDay, Month, DayOfWeek}


@RunWith(classOf[JUnitRunner])
class AsyncRuleBasedCalendarRepositorySpec extends Specification {
  val executorsPool: ExecutorService = Executors.newCachedThreadPool()

  implicit val executors = ExecutionContext.fromExecutorService(executorsPool)

  implicit val entityIOContext = AsyncEntityIOContext()

  val timeout = Duration(30, SECONDS)

  "Rule based calendar" should {
    "Empty day of week" in {
      val name = DayOfWeekCalendarName(Seq())
      val cr = AsyncRuleBasedCalendarRepository()
      val cal = cr.resolve(CalendarId(name, 2014))

      cal map { _.holidays.size } must be_==(0).await(timeout = timeout)
    }

    "Sundays" in {
      val name = DayOfWeekCalendarName(Seq(DayOfWeek.SUNDAY))
      val cr = AsyncRuleBasedCalendarRepository()
      val cal = cr.resolve(CalendarId(name, 2014))

      cal map { _.holidays.size } must be_==(52).await(timeout = timeout)
    }

    "Sundays + Saturdays" in {
      val name = DayOfWeekCalendarName(Seq(DayOfWeek.SUNDAY, DayOfWeek.SATURDAY))
      val cr = AsyncRuleBasedCalendarRepository()
      val cal = cr.resolve(CalendarId(name, 2014))

      cal map { _.holidays.size } must be_==(104).await(timeout = timeout)
    }

    "Empty Month day" in {
      val name = MonthDayCalendarName(Seq())
      val cr = AsyncRuleBasedCalendarRepository()
      val cal = cr.resolve(CalendarId(name, 2014))

      cal map { _.holidays.size } must be_==(0).await(timeout = timeout)
    }

    "December 31st" in {
      val name = MonthDayCalendarName(Seq(MonthDay.of(Month.DECEMBER, 31)))
      val cr = AsyncRuleBasedCalendarRepository()
      val cal = cr.resolve(CalendarId(name, 2014))

      cal map { _.holidays.size } must be_==(1).await(timeout = timeout)
    }

    "Specific days" in {
      val name = MonthDayCalendarName(Seq(MonthDay.of(Month.JANUARY, 1), MonthDay.of(Month.JULY, 4)))
      val cr = AsyncRuleBasedCalendarRepository()
      val cal = cr.resolve(CalendarId(name, 2014))

      cal map { _.holidays.size } must be_==(2).await(timeout = timeout)
    }
  }
}
