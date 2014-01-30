package etude.app.holiday.application.api

import scala.concurrent.{Await, ExecutionContext}
import com.twitter.finatra.Controller
import etude.foundation.calendar.holiday.domain.{CalendarId, NationalHolidayCalendarName, AsyncCalendarRepository}
import etude.foundation.i18n.region.iso.{Country => ISOCountry}
import java.util.concurrent.{Executors, ExecutorService}
import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}
import java.time.LocalDate
import etude.foundation.domain.lifecycle.async.AsyncEntityIOContext
import etude.app.holiday.application.ControllerBase

class Country(val calendarRepository: AsyncCalendarRepository)
  extends ControllerBase {

  def getCountryHoliday(country: String, year: Int, month: Int, day: Int): Try[Boolean] = {
    if (year < 2000 || LocalDate.now.getYear + 1 < year) {
      return Failure(new IllegalArgumentException(s"year must between 2000 and current year + 1: $year"))
    }
    Try {
      val name = NationalHolidayCalendarName(ISOCountry(country))
      val id = CalendarId(name, year)
      val calendar = Await.result(calendarRepository.resolve(id), timeout)

      calendar.isHoliday(LocalDate.of(year, month, day))
    }
  }

  get("/country/:country/:date") {
    request =>
      val countryPattern = "([A-Z]{2})".r
      val datePattern = "([0-9]{4})-([0-1][0-9])-([0-3][0-9])".r

      val countryParam = request.routeParams.getOrElse("country", "")
      val dateParam = request.routeParams.getOrElse("date", "")

      (countryParam, dateParam) match {
        case (countryPattern(country), datePattern(year, month, day)) =>
          getCountryHoliday(country, year.toInt, month.toInt, day.toInt) match {
            case Success(h) => render.json(Map("holiday" -> h)).toFuture
            case Failure(f) =>
              render.notFound.toFuture
          }
        case _ =>
          render.notFound.toFuture
      }
  }
}
