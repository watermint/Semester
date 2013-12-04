package etude.calendar.infrastructure.provider

import etude.http._
import etude.region.Country
import etude.religion.Religion
import java.net.URI
import java.time.LocalDate
import java.util.Locale
import scala.Some
import scala.xml.{Node, XML}
import scala.util.{Success, Failure, Try}
import etude.calendar.domain.{ReligiousHoliday, Holiday, NationalHoliday, CalendarDateSpan}
import etude.calendar.infrastructure.{NoCalendarFoundException, CalendarLoadException}

case class GoogleCalendarHolidays(locale: Locale = Locale.getDefault) {

  def nationalHolidays(span: CalendarDateSpan, country: Country): Try[Seq[Holiday]] = {
    if (supportedRegions.contains(country)) {
      holidays(uri(span, country)) match {
        case Failure(e) => Failure(e)
        case Success(r) =>
          Success(
            r.map(e => NationalHoliday(e.date, Some(e.title), country))
          )
      }
    } else {
      Failure(NoCalendarFoundException("No calendar found for country: " + country))
    }
  }

  def religiousHolidays(span: CalendarDateSpan, religion: Religion): Try[Seq[Holiday]] = {
    if (supportedReligions.contains(religion)) {
      holidays(uri(span, religion)) match {
        case Failure(e) => Failure(e)
        case Success(r) =>
          Success(
            r.map(e => ReligiousHoliday(e.date, Some(e.title)))
          )
      }
    } else {
      Failure(NoCalendarFoundException("No calendar found for religion: " + religion))
    }
  }

  lazy val defaultLanguage = "en"

  lazy val supportedLanguages: Seq[String] = Seq(
    "da", // Danish
    "de", // German
    "en", // English
    "es", // Spanish
    "fi", // Finnish
    "fr", // French
    "it", // Italian
    "ja", // Japanese
    "ko", // Korean
    "nl", // Dutch
    "no", // Norwegian
    "pl", // Polish
    "ru", // Russian
    "sv" // Swedish
  )

  lazy val supportedReligions: Map[Religion, String] = Map(
    Religion.christian -> "christian",
    Religion.islamic -> "islamic",
    Religion.jewish -> "jewish"
  )

  lazy val supportedRegions: Map[Country, String] = Map(
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

  def calendarLanguage(locale: Locale): String = {
    val lang = locale.getLanguage match {
      case "" => Locale.getDefault.getLanguage
      case _ => locale.getLanguage
    }
    if (supportedLanguages.contains(lang)) {
      lang
    } else {
      defaultLanguage
    }
  }

  private def calendarName(country: Country): String = {
    supportedRegions.get(country).get
  }

  private def uri(span: CalendarDateSpan, name: String): URI = {
    new URI(
      "http://www.google.com/calendar/feeds/" +
        calendarLanguage(locale) + "." + name +
        "%23holiday%40group.v.calendar.google.com/public/full?" +
        "start-min=" + span.start +
        "&start-max=" + span.end
    )
  }

  private def uri(span: CalendarDateSpan, country: Country): URI = {
    uri(span, calendarName(country))
  }

  private def uri(span: CalendarDateSpan, religion: Religion): URI = {
    uri(span, religion.id)
  }

  private def holiday(entry: Node): GoogleCalendarHolidays.Entry = {
    GoogleCalendarHolidays.Entry(
      date = LocalDate.parse(((entry \ "when").last \ "@startTime").text),
      title = (entry \ "title").text
    )
  }

  private def holidays(uri: URI): Try[Seq[GoogleCalendarHolidays.Entry]] = {
    GoogleCalendarHolidays.cacheUpdate(locale, uri, {
      uri =>
        uri.get match {
          case Failure(e) => Failure(e)
          case Success(r) =>
            r.statusCode match {
              case c: StatusSuccessful =>
                Success((XML.load(r.contentAsString) \ "entry").map(holiday))
              case c: StatusInternalServerError =>
                Failure(NoCalendarFoundException("No calendar found for: " + uri))
              case c =>
                Failure(CalendarLoadException("Load failed with HTTP status code: " + c.code))
            }
        }
    })
  }

}

object GoogleCalendarHolidays {

  private case class Entry(date: LocalDate, title: String)

  private val cache: scala.collection.mutable.Map[Pair[Locale, URI], Seq[Entry]] = scala.collection.mutable.Map()

  private def cacheUpdate(locale: Locale, uri: URI, result: URI => Try[Seq[Entry]]): Try[Seq[Entry]] = {
    val key = Pair(locale, uri)
    cache.get(key) match {
      case Some(cached) => Success(cached)
      case _ =>
        result(uri) match {
          case Failure(e) => Failure(e)
          case Success(r) =>
            cache.update(key, r)
            Success(r)
        }
    }
  }

}