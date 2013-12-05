package etude.commons.domain.calendar

trait Holiday extends CalendarDate {
  val title: Option[String]
}
