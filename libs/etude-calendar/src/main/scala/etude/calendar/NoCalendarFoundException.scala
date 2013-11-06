package etude.calendar

class NoCalendarFoundException(message: String, cause: Throwable) extends RuntimeException(message, cause)

object NoCalendarFoundException {
  def apply(message: String, cause: Throwable) = new NoCalendarFoundException(message, cause)

  def apply(message: String) = new NoCalendarFoundException(message, null)

  def apply(cause: Throwable) = new NoCalendarFoundException(null, cause)
}
