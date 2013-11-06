package etude.aggregation

/**
 *
 */
case class AggregatedException[T](causes: Map[T, Exception])
  extends RuntimeException {

}
