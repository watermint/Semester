package etude.calendar.domain

case class AggregatedException[T](causes: Map[T, Throwable])
  extends RuntimeException