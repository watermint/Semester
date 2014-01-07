package etude.foundation.i18n.region.iso

import java.util.Locale

sealed trait Country {
  val code: String

  val locale: Locale

  val isUserAssigned: Boolean
}

object Country {
  def apply(iso3166: String): Country = {
    iso3166 match {
      case null => throw new IllegalArgumentException("null not allowed")
      case _ => CountryImpl(iso3166.toUpperCase)
    }
  }

  def unapply(country: Country): Option[String] = Some(country.code)

  def isValidCode(code: String): Boolean = {
    DecodingTable.getState(code.toUpperCase) match {
      case CodeState.Unassigned => false
      case _ => true
    }
  }

  val AUSTRALIA = Country("AU")
  val AUSTRIA = Country("AT")
  val BRAZIL = Country("BR")
  val CANADA = Country("CA")
  val CHINA = Country("CN")
  val DENMARK = Country("DK")
  val GERMANY = Country("DE")
  val FINLAND = Country("FI")
  val FRANCE = Country("FR")
  val GREECE = Country("GR")
  val HONGKONG = Country("HK")
  val INDIA = Country("IN")
  val INDONESIA = Country("ID")
  val ITALY = Country("IT")
  val JAPAN = Country("JP")
  val MALAYSIA = Country("MY")
  val MEXICO = Country("MX")
  val NEW_ZEALAND = Country("NZ")
  val NORWAY = Country("NO")
  val PHILIPPINES = Country("PH")
  val POLAND = Country("PL")
  val PORTUGAL = Country("PT")
  val RUSSIAN = Country("RU")
  val SINGAPORE = Country("SG")
  val SOUTH_AFRICA = Country("ZA")
  val KOREA = Country("KR")
  val SPAIN = Country("ES")
  val SWEDEN = Country("SE")
  val TAIWAN = Country("TW")
  val THAILAND = Country("TH")
  val UNITED_STATES = Country("US")
  val UNITED_KINGDOM = Country("GB")
  val VIET_NAM = Country("VN")
}

private[iso]
case class CountryImpl(code: String) extends Country {
  require(code.matches("[A-Z]{2}"))
  require(Country.isValidCode(code))

  val locale: Locale = new Locale.Builder().setRegion(code).build()

  lazy val isUserAssigned: Boolean = DecodingTable.getState(code) == CodeState.UserAssigned
}
