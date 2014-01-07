package etude.foundation.i18n.region.iso

import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mutable.Specification

@RunWith(classOf[JUnitRunner])
class CountrySpec extends Specification {
  "ISO 3166-1 Country Code" should {
    "be two letters" in {
      Country("JP") must beAnInstanceOf[Country]
      Country("J") must throwA[IllegalArgumentException]
      Country("") must throwA[IllegalArgumentException]
      Country(null) must throwA[IllegalArgumentException]
    }

    "be valid code" in {
      Country("AB") must throwA[IllegalArgumentException]
      Country("AH") must throwA[IllegalArgumentException]
    }

    "match user assigned code" in {
      Country("AA").isUserAssigned must beTrue
      Country("QM").isUserAssigned must beTrue
      Country("QZ").isUserAssigned must beTrue
      Country("XA").isUserAssigned must beTrue
      Country("XZ").isUserAssigned must beTrue
      Country("ZZ").isUserAssigned must beTrue

      Country("AD").isUserAssigned must beFalse
      Country("JP").isUserAssigned must beFalse
    }
  }
}
