package etude.money

import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mutable.Specification
import java.util.Locale

/**
 *
 */
@RunWith(classOf[JUnitRunner])
class MoneyTest extends Specification {
  "Currency" should {
    "Instantiate" in {
      Currency("JPY") must beAnInstanceOf[Currency]
      Currency("USD") must beAnInstanceOf[Currency]
      Currency("ZZZ") must throwA[InvalidCurrencyException]

      Currency("JPY", 2) must beAnInstanceOf[Currency]
      Currency("USD", 2) must beAnInstanceOf[Currency]
      Currency("ZZZ", 2) must throwA[InvalidCurrencyException]

      Currency("JPY").displayName() must equalTo(java.util.Currency.getInstance("JPY").getDisplayName)
      Currency("JPY").displayName(Locale.forLanguageTag("en-US")) must equalTo("Japanese Yen")
      Currency("USD").symbol() must equalTo(java.util.Currency.getInstance("USD").getSymbol)
      Currency("USD").symbol(Locale.forLanguageTag("en-US")) must equalTo("$")

      Currency("JPY").code must equalTo("JPY")
    }
  }

  "Money" should {
    "Arithmetic" in {
      (Money(7, Currency("JPY")) + 11).amount must equalTo(18)
      (Money(7, Currency("JPY")) - 11).amount must equalTo(-4)
      (Money(7, Currency("JPY")) * 11).amount must equalTo(77)
      (Money(7, Currency("JPY")) / 11).amount must equalTo(BigDecimal(7) / BigDecimal(11))
      (Money(11, Currency("JPY")) % 7).amount must equalTo(4)
      (Money(11, Currency("JPY")) > 7) must beTrue
      (Money(11, Currency("JPY")) >= 7) must beTrue
      (Money(11, Currency("JPY")) < 7) must beFalse
      (Money(11, Currency("JPY")) <= 7) must beFalse
    }
  }
}
