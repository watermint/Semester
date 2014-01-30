package etude.app.holiday.infrastructure.named

import etude.app.holiday.domain.{BusinessCalendar, BusinessCalendarId, SyncBusinessCalendarRepository}
import etude.foundation.domain.lifecycle.EntityIOContext
import scala.util.{Failure, Success, Try}
import etude.foundation.calendar.holiday.domain.CompositeCalendarName
import etude.foundation.calendar.CalendarNotFoundException
import org.json4s.native.JsonMethods
import org.json4s._
import java.time.{MonthDay, DayOfWeek}
import etude.foundation.i18n.region.iso.Country
import org.slf4j.LoggerFactory
import java.io.File
import scala.io.Source

case class SyncBusinessCalendarRepositoryOnFile(path: File)
  extends SyncBusinessCalendarRepository {

  type This <: SyncBusinessCalendarRepositoryOnFile

  protected val logger = LoggerFactory.getLogger(getClass)

  /*
   * JSON format.
   *
   * {
   *  "name":
   *   {"country": "<ISO-3166-1 ALPHA2",
   *    "dayOfWeek": ["SUNDAY", "SATURDAY"],
   *    "monthDay": ["--01-01", "--01-02", "--<MONTH>-<DAY OF MONTH>"]
   *   }
   * }
   */
  def loadCalendars(): Try[Map[String, CompositeCalendarName]] = {
    val jsonContent = Source.fromFile(path).getLines().mkString

    val cals: List[(String, CompositeCalendarName)] = for {
      JObject(file) <- JsonMethods.parse(jsonContent)
      JField(name, JObject(definition)) <- file
      JField("country", JString(country)) <- definition
      JField("dayOfWeek", JArray(dayOfWeeks)) <- definition
      JField("monthDay", JArray(monthDays)) <- definition
    } yield {
      val dow = for {
        JString(dayOfWeek) <- dayOfWeeks
      } yield {
        DayOfWeek.valueOf(dayOfWeek)
      }
      val md = for {
        JString(monthDay) <- monthDays
      } yield {
        MonthDay.parse(monthDay)
      }

      name -> CompositeCalendarName(
        country = Country(country),
        dayOfWeek = dow,
        monthDay = md
      )
    }
    Success(cals.toMap)
  }

  val calendars: Map[String, CompositeCalendarName] = {
    loadCalendars() match {
      case Success(c) => c
      case Failure(f) =>
        logger.error(s"Failed to load from path: $path", f)
        Map()
    }
  }

  def containsByIdentity(identity: BusinessCalendarId)(implicit context: EntityIOContext[Try]): Try[Boolean] = {
    Success(calendars.contains(identity.name))
  }

  def resolve(identity: BusinessCalendarId)(implicit context: EntityIOContext[Try]): Try[BusinessCalendar] = {
    calendars.get(identity.name) match {
      case Some(c) =>
        Success(
          new BusinessCalendar(
            name = identity,
            country = c.country,
            dayOfWeek = c.dayOfWeek,
            monthDay = c.monthDay
          )
        )
      case _ =>
        Failure(CalendarNotFoundException(s"Calendar not found for ${identity.name}"))
    }
  }
}
