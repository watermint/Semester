package etude.foundation.calendar.holiday.infrastructure.google

import etude.foundation.calendar.holiday.domain.{NationalHoliday, NationalHolidays, NationalHolidaysId, AsyncNationalHolidaysRepository}
import etude.foundation.domain.lifecycle.EntityIOContext
import scala.concurrent.{future, Future}
import etude.foundation.i18n.region.iso.Country
import java.net.URI
import etude.foundation.http.{Response, Client}
import etude.foundation.calendar.CalendarNotFoundException
import scala.util.{Success, Failure}
import scala.xml.{XML, Node}
import java.time.LocalDate

case class AsyncGoogleNationalHolidaysRepository()
  extends AsyncNationalHolidaysRepository {
  type This <: AsyncGoogleNationalHolidaysRepository

  lazy val client: Client = new Client

  val titleLanguage: String = "en"

  val supportedCountries: Map[Country, String] = Map(
    Country.AUSTRALIA -> "australian",
    Country.AUSTRIA -> "austrian",
    Country.BRAZIL -> "brazilian",
    Country.CANADA -> "canadian",
    Country.CHINA -> "china",
    Country.DENMARK -> "danish",
    Country.FINLAND -> "finnish",
    Country.FRANCE -> "french",
    Country.GERMANY -> "german",
    Country.GREECE -> "greek",
    Country.HONGKONG -> "hong_kong",
    Country.INDIA -> "indian",
    Country.INDONESIA -> "indonesian",
    Country.ITALY -> "italian",
    Country.JAPAN -> "japanese",
    Country.KOREA -> "south_korea",
    Country.MALAYSIA -> "malaysia",
    Country.MEXICO -> "mexican",
    Country.NEW_ZEALAND -> "new_zealand",
    Country.NORWAY -> "norwegian",
    Country.PHILIPPINES -> "philippines",
    Country.POLAND -> "polish",
    Country.PORTUGAL -> "portuguese",
    Country.RUSSIAN -> "russian",
    Country.SINGAPORE -> "singapore",
    Country.SOUTH_AFRICA -> "sa",
    Country.SPAIN -> "spain",
    Country.SWEDEN -> "swedish",
    Country.TAIWAN -> "taiwan",
    Country.THAILAND -> "thai",
    Country.UNITED_KINGDOM -> "uk",
    Country.UNITED_STATES -> "usa",
    Country.VIET_NAM -> "vietnamese"
  )

  def calendarUri(name: String, year: Int): URI = {
    new URI(
      s"http://www.google.com/calendar/feeds/${titleLanguage}.${name}" +
        s"%23holiday%40group.v.calendar.google.com/public/full?start-min=${year}-01-01&start-max=${year}-12-31"
    )
  }

  def holiday(entry: Node): NationalHoliday = {
    NationalHoliday(
      date = LocalDate.parse(((entry \ "when").last \ "@startTime").text),
      title = (entry \ "title").text
    )
  }

  def holidays(response: Response): Seq[NationalHoliday] = {
    (XML.loadString(response.contentAsString) \ "entry").map(holiday)
  }

  def resolve(identity: NationalHolidaysId)(implicit context: EntityIOContext[Future]): Future[NationalHolidays] = {
    implicit val executionContext = getExecutionContext(context)
    future {
      supportedCountries.get(identity.country) match {
        case None =>
          throw CalendarNotFoundException(s"Calendar name not found for country: ${identity.country}")
        case Some(calendarName) =>
          client.get(calendarUri(calendarName, identity.year)) match {
            case Failure(failure) =>
              throw failure
            case Success(response) =>
              new NationalHolidays(identity, holidays(response))
          }
      }
    }
  }

  def containsByIdentity(identity: NationalHolidaysId)(implicit context: EntityIOContext[Future]): Future[Boolean] = {
    implicit val executionContext = getExecutionContext(context)
    future {
      supportedCountries.contains(identity.country)
    }
  }
}
