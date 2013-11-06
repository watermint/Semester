package etude.money

import java.util.{Currency => JavaCurrency, Locale}

/**
  */
case class Currency(javaCurrency: JavaCurrency, fractionDigits: Int) {
  lazy val code: String = javaCurrency.getCurrencyCode

  def displayName(): String = javaCurrency.getDisplayName

  def displayName(locale: Locale): String = javaCurrency.getDisplayName(locale)

  def symbol(): String = javaCurrency.getSymbol

  def symbol(locale: Locale): String = javaCurrency.getSymbol(locale)
}

object Currency {
  def apply(currencyCode: String): Currency = {
    try {
      val javaCurrency = JavaCurrency.getInstance(currencyCode)
      Currency(javaCurrency, javaCurrency.getDefaultFractionDigits)
    } catch {
      case t: IllegalArgumentException =>
        throw InvalidCurrencyException(currencyCode, t)
    }
  }

  def apply(currencyCode: String, fractionDigits: Int): Currency = {
    try {
      Currency(JavaCurrency.getInstance(currencyCode), fractionDigits)
    } catch {
      case t: IllegalArgumentException =>
        throw InvalidCurrencyException(currencyCode, t)
    }
  }
}