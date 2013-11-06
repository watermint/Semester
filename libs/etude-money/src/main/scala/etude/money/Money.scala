package etude.money

/**
 *
 */
case class Money(amount: BigDecimal, currency: Currency) {
  def pair(other: BigDecimal): MoneyPair = MoneyPair(this, other)

  def pair(other: Money): MoneyPair = MoneyPair(this, other)

  def +(other: BigDecimal): Money = pair(other).add

  def +(other: Money): Money = pair(other).add

  def -(other: BigDecimal): Money = pair(other).subtract

  def -(other: Money): Money = pair(other).subtract

  def *(other: BigDecimal): Money = pair(other).multiply

  def *(other: Money): Money = pair(other).multiply

  def /(other: BigDecimal): Money = pair(other).divide

  def /(other: Money): Money = pair(other).divide

  def %(other: BigDecimal): Money = pair(other).remainder

  def %(other: Money): Money = pair(other).remainder

  def <(other: BigDecimal): Boolean = pair(other).xIsLess

  def <(other: Money): Boolean = pair(other).xIsLess

  def <=(other: BigDecimal): Boolean = pair(other).xIsLessOrEquals

  def <=(other: Money): Boolean = pair(other).xIsLessOrEquals

  def >(other: BigDecimal): Boolean = pair(other).xIsGrater

  def >(other: Money): Boolean = pair(other).xIsGrater

  def >=(other: BigDecimal): Boolean = pair(other).xIsGraterOrEquals

  def >=(other: Money): Boolean = pair(other).xIsGraterOrEquals

  lazy val intValue: Int = amount.intValue()

  lazy val longValue: Long = amount.longValue()
}
