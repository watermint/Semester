package etude.calendar.domain


trait Holiday extends CalendarDate {
  val title: Option[String]
}
