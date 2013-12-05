package etude.commons.domain.calendar

case class AggregatedException[T](causes: Map[T, Throwable])
  extends RuntimeException