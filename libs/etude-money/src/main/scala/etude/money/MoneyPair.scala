package etude.money

/**
 *
 */
case class MoneyPair(amountX: BigDecimal,
                     amountY: BigDecimal,
                     currency: Currency) {
  // TODO: ensure fraction

  def compute(f: (BigDecimal, BigDecimal) => BigDecimal): Money = Money(f(amountX, amountY), currency)

  lazy val add: Money = compute {(x, y) => x + y}

  lazy val divide: Money = compute {(x, y) => x / y}

  lazy val multiply: Money = compute {(x, y) => x * y}

  lazy val subtract: Money = compute {(x, y) => x - y}

  lazy val remainder: Money = compute {(x, y) => x % y}

  lazy val xIsGrater: Boolean = amountX > amountY

  lazy val xIsGraterOrEquals: Boolean = amountX >= amountY

  lazy val xIsLess: Boolean = amountX < amountY

  lazy val xIsLessOrEquals: Boolean = amountX <= amountY
}

object MoneyPair {
  def apply(x: Money, y: Money): MoneyPair = {
    if (x.currency != y.currency) {
      throw IncompatibleCurrencyException("Currency for x[" + x.currency + "] and y[" + y.currency + "] are not compatible")
    }
    MoneyPair(
      x.amount,
      y.amount,
      x.currency
    )
  }

  def apply(x: Money, y: BigDecimal): MoneyPair = {
    MoneyPair(
      x.amount,
      y,
      x.currency
    )
  }
}