package etude.money

/**
 *
 */
class IncompatibleCurrencyException(message: String, cause: Throwable) extends RuntimeException(message, cause)

object IncompatibleCurrencyException {
  def apply(message: String, cause: Throwable) = new IncompatibleCurrencyException(message, cause)

  def apply(message: String) = new IncompatibleCurrencyException(message, null)

  def apply(cause: Throwable) = new IncompatibleCurrencyException(null, cause)
}
