package etude.app.holiday.application.api

import etude.foundation.calendar.holiday.domain.{NamedCalendarName, CalendarId, AsyncCalendarRepository}
import etude.app.holiday.application.ControllerBase
import scala.util.{Success, Failure, Try}
import java.time.LocalDate
import scala.concurrent.Await

class Business(val calendarRepository: AsyncCalendarRepository)
  extends ControllerBase {

  def getNamedHoliday(name: String, year: Int, month: Int, day: Int): Try[Boolean] = {
    if (year < 2000 || LocalDate.now.getYear + 1 < year) {
      return Failure(new IllegalArgumentException(s"year must between 2000 and current year + 1: $year"))
    }
    Try {
      val calName = NamedCalendarName(name)
      val id = CalendarId(calName, year)
      val calendar = Await.result(calendarRepository.resolve(id), timeout)

      calendar.isHoliday(LocalDate.of(year, month, day))
    }
  }

  get("/business/:name/:date") {
    request =>
      val datePattern = "([0-9]{4})-([0-1][0-9])-([0-3][0-9])".r

      val nameParam = request.routeParams.getOrElse("name", "")
      val dateParam = request.routeParams.getOrElse("date", "")

      dateParam match {
        case datePattern(year, month, day) =>
          getNamedHoliday(nameParam, year.toInt, month.toInt, day.toInt) match {
            case Success(h) => render.json(Map("holiday" -> h)).toFuture
            case Failure(f) =>
              render.notFound.toFuture
          }
        case _ =>
          render.notFound.toFuture
      }
  }
}
