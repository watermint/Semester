package etude.region

import java.util.Locale

/**
 * ISO 3166-1 country
 */
case class Country(iso3166: String) {
  lazy val locale: Locale = new Locale.Builder().setRegion(iso3166).build()
}

object Country {
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