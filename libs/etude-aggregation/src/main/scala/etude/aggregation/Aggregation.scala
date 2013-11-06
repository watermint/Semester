package etude.aggregation

/**
 *
 */
object Aggregation {
  def aggregate[R, V](funcSeq: Map[R, () => Either[Exception, V]]): Either[Exception, Seq[V]] = {
    val results = funcSeq.map {
      f =>
        f._1 -> f._2()
    }

    if (results.forall(_._2.isRight)) {
      Right(results.values.map(_.right.get).toSeq)
    } else {
      Left(AggregatedException(results.collect { case Pair(r, Left(l)) => r -> l}))
    }
  }
}
