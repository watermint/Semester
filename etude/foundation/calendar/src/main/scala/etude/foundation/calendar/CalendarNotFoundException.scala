package etude.foundation.calendar

case class CalendarNotFoundException(message: String)
  extends Exception(message)
