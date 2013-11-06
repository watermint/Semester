package etude.money

/**
 *
 */
class InvalidCurrencyException(message: String, cause: Throwable) extends RuntimeException(message, cause)

object InvalidCurrencyException {
  def apply(message: String, cause: Throwable) = new InvalidCurrencyException(message, cause)

  def apply(message: String) = new InvalidCurrencyException(message, null)

  def apply(cause: Throwable) = new InvalidCurrencyException(null, cause)
}
